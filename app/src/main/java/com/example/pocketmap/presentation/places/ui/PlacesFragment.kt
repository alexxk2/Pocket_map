package com.example.pocketmap.presentation.places.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pocketmap.R
import com.example.pocketmap.databinding.FragmentPlacesBinding
import com.example.pocketmap.domain.models.Place
import com.example.pocketmap.presentation.places.models.PlacesScreenState
import com.example.pocketmap.presentation.places.view_model.PlacesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlacesFragment : Fragment() {

    private var _binding: FragmentPlacesBinding? = null
    private val binding get() = _binding!!
    private lateinit var placesAdapter: PlacesAdapter
    private val viewModel: PlacesViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            navigateBackWithoutId()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlacesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerView()
        with(viewModel) {
            getAllPlaces()

            listOfPlaces.observe(viewLifecycleOwner) { listOfPlaces ->
                placesAdapter.submitList(listOfPlaces)
            }

            screenState.observe(viewLifecycleOwner) { screenState ->
                manageScreenContent(screenState)
            }
        }

        with(binding) {
            buttonBackToMap.setOnClickListener {
                navigateBackWithoutId()
            }

            topAppBar.setNavigationOnClickListener {
                navigateBackWithoutId()
            }

            topAppBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.delete_all_items -> {

                        showDeleteConfirmationDialog()
                        true
                    }

                    else -> {
                        findNavController().navigateUp()
                        true
                    }
                }
            }
        }

    }

    private fun manageScreenContent(screenState: PlacesScreenState) {

        when (screenState) {
            PlacesScreenState.Content -> {
                with(binding) {
                    placesRecyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                    noContentLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE

                }
            }

            PlacesScreenState.Empty -> {
                with(binding) {
                    placesRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    noContentLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE

                }
            }

            PlacesScreenState.Error -> {
                with(binding) {
                    placesRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.GONE
                    noContentLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE

                }
            }

            PlacesScreenState.Loading -> {
                with(binding) {
                    placesRecyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    noContentLayout.visibility = View.GONE
                    errorLayout.visibility = View.GONE

                }
            }
        }
    }

    private fun setRecyclerView() {
        placesAdapter = PlacesAdapter(requireContext(), object : PlacesAdapter.PlacesClickListener {
            override fun onPlaceClick(place: Place) {

                val action =
                    PlacesFragmentDirections.actionPlacesFragmentToMapFragment(placeId = place.id)
                findNavController().navigate(action)
            }
        })
        binding.placesRecyclerView.adapter = placesAdapter
        binding.placesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.placesRecyclerView.setHasFixedSize(true)
    }

    private fun showDeleteConfirmationDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title_delete))
            .setMessage(getString(R.string.alert_dialog_message_delete_all))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->
                viewModel.deleteAllPlaces()
            }
            .show()
    }

    private fun navigateBackWithoutId() {
        val action = PlacesFragmentDirections.actionPlacesFragmentToMapFragment()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

    }
}