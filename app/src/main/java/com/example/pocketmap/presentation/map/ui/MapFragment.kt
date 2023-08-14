package com.example.pocketmap.presentation.map.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pocketmap.R
import com.example.pocketmap.databinding.FragmentMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.ScreenPoint
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment() {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        MapKitFactory.initialize(requireContext().applicationContext)
        initialMovement()


        binding.addNewPlaceButton.setOnClickListener {
            val centerX = binding.yandexMapsView.width/2f
            val centerY = binding.yandexMapsView.height/2f
            val centerPoint = ScreenPoint(centerX,centerY)
            val worldPoint = binding.yandexMapsView.screenToWorld(centerPoint)

            binding.yandexMapsView.map.mapObjects.addPlacemark(
                worldPoint,
                ImageProvider.fromResource(requireContext(), R.drawable.image_new_place)
            )
        }
    }

    private fun initialMovement(){
        binding.yandexMapsView.map.move(
            CameraPosition(
                Point(59.945933, 30.320045),
                14.0f,
                0.0f,
                0.0f
            ), Animation(Animation.Type.SMOOTH, 5f), null
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
}