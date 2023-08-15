package com.example.pocketmap.presentation.map.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pocketmap.R
import com.example.pocketmap.databinding.FragmentMapBinding
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.presentation.map.view_model.MapViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

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
        initialMovement()




        binding.topAppBar.setOnMenuItemClickListener { menuitem ->
            when (menuitem.itemId) {
                R.id.save_place -> {

                    val newPoint = createWorldPointInCenter()
                    val newPlace = Place(
                        lat = newPoint.latitude,
                        lon = newPoint.longitude
                    )

                    addPlaceMarkOnMap(newPoint)
                    viewModel.addNewPlace(place = newPlace)
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

    private fun initialMovement() {
        binding.yandexMapsView.map.move(
            CameraPosition(
                Point(59.945933, 30.320045),
                14.0f,
                0.0f,
                0.0f
            ), Animation(Animation.Type.SMOOTH, 5f), null
        )
    }

    private fun addPlaceMarkOnMap(worldPoint: Point) {
        binding.yandexMapsView.map.mapObjects.addPlacemark(
            worldPoint,
            ImageProvider.fromResource(requireContext(), R.drawable.image_new_place)
        )
    }

    private fun createWorldPointInCenter(): Point {
        with(binding) {
            val centerX = yandexMapsView.width / 2f
            val centerY = yandexMapsView.height / 2f
            val centerPoint = ScreenPoint(centerX, centerY)
            return yandexMapsView.screenToWorld(centerPoint)
        }
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
}