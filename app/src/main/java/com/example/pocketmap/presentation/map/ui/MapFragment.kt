package com.example.pocketmap.presentation.map.ui

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
import com.google.android.material.snackbar.Snackbar
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MapViewModel by viewModel()
    private lateinit var listOfPlaces: List<Place>
    private var currentZoomValue = INITIAL_ZOOM_VALUE
    private var clickedPlaceId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            clickedPlaceId = it.getInt(PLACE_ID)
        }
    }

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

        with(viewModel) {
            getAllPlaces()

            listOfPlaces.observe(viewLifecycleOwner) { newListOfPlaces ->
                this@MapFragment.listOfPlaces = newListOfPlaces
                manageSpotsDrawing(newListOfPlaces)
                initialMapCameraMovement(newListOfPlaces)
            }

            screenState.observe(viewLifecycleOwner) { screenState ->
                manageScreenContent(screenState = screenState)
            }
        }

        with(binding) {
            zoomInButton.setOnClickListener { zoomIn() }
            zoomOutButton.setOnClickListener { zoomOut() }

            topAppBar.setOnMenuItemClickListener { menuitem ->
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

    }

    private fun initialMapCameraMovement(listOfPlaces: List<Place>) {
        if (clickedPlaceId != -1) {
            val clickedPlace = listOfPlaces.first { it.id == clickedPlaceId }
            val clickedPoint = Point(clickedPlace.lat, clickedPlace.lon)
            moveMapCamera(clickedPoint)

        } else if (listOfPlaces.isEmpty()) {
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

    private fun clearMapFromAllObjects() {
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

        latitudeEditText.text = String.format(Locale.US, "%.4f", newPoint.latitude)
        longitudeEditText.text = String.format(Locale.US, "%.4f", newPoint.longitude)

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
            name = getString(R.string.place_name_text, listOfPlaces.size + 1),
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

                    if (latitudeEditText.text.toString().toDouble() !in (-90f..90f)
                    ) {
                        showWrongInputSnackbar(R.string.snackbar_message_latitude_text)

                    } else if (longitudeEditText.text.toString().toDouble() !in (-180f..180f)
                    ) {
                        showWrongInputSnackbar(R.string.snackbar_message_longitude_text)

                    } else {
                        val newPointEdited = Point(
                            latitudeEditText.text.toString().toDouble(),
                            longitudeEditText.text.toString().toDouble()
                        )
                        val newPlaceEdited = Place(
                            name = getString(R.string.place_name_text, listOfPlaces.size + 1),
                            lat = newPointEdited.latitude,
                            lon = newPointEdited.longitude
                        )
                        addPlaceMarkOnMap(newPointEdited)
                        viewModel.addNewPlace(place = newPlaceEdited)
                    }


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
                point, INITIAL_ZOOM_VALUE, 0.0f, 0.0f
            ), Animation(Animation.Type.SMOOTH, MAP_MOVEMENT_DURATION_LONG), null
        )
    }

    private fun zoomMapCamera(zoomValue: Float) {
        binding.yandexMapsView.map.move(
            CameraPosition(
                createWorldPointInCenter(), zoomValue, 0.0f, 0.0f
            ), Animation(Animation.Type.SMOOTH, MAP_MOVEMENT_DURATION_SHORT), null
        )
    }

    private fun zoomIn() {
        if (currentZoomValue >= binding.yandexMapsView.map.maxZoom) return

        currentZoomValue += 1.0f
        zoomMapCamera(currentZoomValue)
    }

    private fun zoomOut() {
        if (currentZoomValue <= binding.yandexMapsView.map.minZoom) return

        currentZoomValue -= 1.0f
        zoomMapCamera(currentZoomValue)
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

    private fun hideKeyboard() {
        val inputMethodManager =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.mapsConstraintLayout.windowToken, 0)
    }

    private fun showWrongInputSnackbar(stringId: Int) {
        hideKeyboard()
        Snackbar.make(
            binding.mapsConstraintLayout,
            stringId,
            TOAST_DURATION
        ).setAction(R.string.snack_bar_action_text) {
            showSaveNewPlaceConfirmationDialog()
        }.setActionTextColor(Color.parseColor("#9CE1FF")).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val MAP_MOVEMENT_DURATION_LONG = 3f
        const val MAP_MOVEMENT_DURATION_SHORT = 0.5f
        const val INITIAL_ZOOM_VALUE = 14.0f
        const val PLACE_ID = "placeId"
        const val TOAST_DURATION = 8000
    }
}

