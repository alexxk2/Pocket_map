package com.example.pocketmap.presentation.map.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pocketmap.R
import com.example.pocketmap.databinding.FragmentMapBinding
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.presentation.map.models.MapScreenState
import com.example.pocketmap.presentation.map.view_model.MapViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModel()
    private lateinit var listOfPlaces: List<Place>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MapKitFactory.initialize(requireContext().applicationContext)

        viewModel.getAllPlaces()

        viewModel.listOfPlaces.observe(viewLifecycleOwner) { newListOfPlaces ->
            listOfPlaces = newListOfPlaces
            manageSpotsDrawing(newListOfPlaces)
            initialMapCameraMovement(newListOfPlaces)
        }

        viewModel.screenState.observe(viewLifecycleOwner) { screenState ->
            manageScreenContent(screenState = screenState)
        }

        binding.topAppBar.setOnMenuItemClickListener { menuitem ->
            when (menuitem.itemId) {
                R.id.save_place -> {

                    showSaveNewPlaceConfirmationDialog()
                    true
                }

                else -> {
                    val action = MapFragmentDirections.actionMapFragmentToPlacesFragment()
                    findNavController().navigate(action)
                    true
                }
            }
        }
    }

    private fun initialMapCameraMovement(listOfPlaces: List<Place>) {
        if (listOfPlaces.isEmpty()) {
            val defaultPoint = Point(59.945933, 30.320045)
            moveMapCamera(defaultPoint)
        } else {
            val lastCreatedPoint = Point(listOfPlaces.last().lat, listOfPlaces.last().lon)
            moveMapCamera(lastCreatedPoint)
        }
    }

    private fun addPlaceMarkOnMap(worldPoint: Point) {
        binding.yandexMapsView.map.mapObjects.addPlacemark(
            worldPoint,
            ImageProvider.fromResource(requireContext(), R.drawable.image_new_place)
        )
    }

    private fun clearMapFromAllObjects(){
        binding.yandexMapsView.map.mapObjects.clear()
    }

    private fun createWorldPointInCenter(): Point {
        with(binding) {
            val centerX = yandexMapsView.width / 2f
            val centerY = yandexMapsView.height / 2f
            val centerPoint = ScreenPoint(centerX, centerY)
            return yandexMapsView.screenToWorld(centerPoint)
        }
    }



    private fun createAndInflateDialogView(newPoint: Point): View {
        val dialogView = View.inflate(requireContext(), R.layout.save_dialog_layout, null)
        val latitudeEditText: TextView = dialogView.findViewById(R.id.edit_latitude)
        val longitudeEditText: TextView = dialogView.findViewById(R.id.edit_longitude)

        latitudeEditText.text = String.format("%.4f", newPoint.latitude)
        longitudeEditText.text = String.format("%.4f", newPoint.longitude)

        return dialogView
    }

    private fun isDataInDialogEdited(
        latEditText: TextView,
        lonEditText: TextView,
        newPoint: Point
    ): Boolean = latEditText.text.toString() != String.format(
        "%.4f",
        newPoint.latitude
    ) || lonEditText.text.toString() != String.format(
        "%.4f",
        newPoint.longitude
    )


    private fun showSaveNewPlaceConfirmationDialog() {

        val newPoint = createWorldPointInCenter()
        val newPlace = Place(
            lat = newPoint.latitude,
            lon = newPoint.longitude
        )

        val dialogView = createAndInflateDialogView(newPoint = newPoint)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title_save))
            .setView(dialogView)
            .setMessage(getString(R.string.alert_dialog_message_save_new_place))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->

                val latitudeEditText: TextView = dialogView.findViewById(R.id.edit_latitude)
                val longitudeEditText: TextView = dialogView.findViewById(R.id.edit_longitude)

                if (isDataInDialogEdited(latitudeEditText, longitudeEditText, newPoint)) {
                    val newPointEdited = Point(
                        latitudeEditText.text.toString().toDouble(),
                        longitudeEditText.text.toString().toDouble()
                    )
                    val newPlaceEdited = Place(
                        lat = newPointEdited.latitude,
                        lon = newPointEdited.longitude
                    )
                    addPlaceMarkOnMap(newPointEdited)
                    viewModel.addNewPlace(place = newPlaceEdited)
                } else {
                    addPlaceMarkOnMap(newPoint)
                    viewModel.addNewPlace(place = newPlace)
                }
            }
            .show()
    }

    private fun manageScreenContent(screenState: MapScreenState) {
        when (screenState) {
            MapScreenState.Content -> {
                with(binding) {
                    mapProgressBar.visibility = View.GONE
                    pointerView.visibility = View.VISIBLE
                }
            }

            MapScreenState.Loading ->
                with(binding) {
                    mapProgressBar.visibility = View.VISIBLE
                    pointerView.visibility = View.GONE
                }

            MapScreenState.Error -> showErrorToast()
        }
    }

    private fun showErrorToast() {
        Toast.makeText(requireContext(), R.string.error_toast, Toast.LENGTH_SHORT).show()
    }

    private fun manageSpotsDrawing(listOfPlaces: List<Place>) {
        if (listOfPlaces.isEmpty()) {
            clearMapFromAllObjects()
            return
        }

        listOfPlaces.forEach { place ->
            val tempPoint = Point(place.lat, place.lon)
            addPlaceMarkOnMap(tempPoint)
        }
    }

    private fun moveMapCamera(point: Point) {
        binding.yandexMapsView.map.move(
            CameraPosition(
                point, 14.0f, 0.0f, 0.0f
            ), Animation(Animation.Type.SMOOTH, MAP_MOVEMENT_DURATION), null
        )
    }

    override fun onStop() {
        binding.yandexMapsView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()

        MapKitFactory.getInstance().onStart()
        binding.yandexMapsView.onStart()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object{
        const val MAP_MOVEMENT_DURATION = 3f
    }
}

