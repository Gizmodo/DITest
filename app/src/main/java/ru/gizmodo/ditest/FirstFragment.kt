package ru.gizmodo.ditest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.gizmodo.ditest.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val viewModel: FirstViewModel by lazy {
        getViewModel { FirstViewModel() }
    }

    init {
        App.instance().appGraph.embed(this)
    }

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        viewModel.urovoScannerLiveData.observe(viewLifecycleOwner) { it ->
            binding.textviewFirst.text = it
            Log.d("666", it)
        }

        viewModel.superScannerLiveData.observe(viewLifecycleOwner) { it: SuperResults ->
            Log.d("666", "Значения со слушателей: " + it.toString())
            with(binding) {
                txtUrovoKeyboard.text = it.isUrovoEnterPushed.toString()
                txtIDataScanner.text = it.idata
                txtUrovoScanner.text = it.urovo
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}