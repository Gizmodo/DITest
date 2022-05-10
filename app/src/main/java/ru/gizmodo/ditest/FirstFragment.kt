package ru.gizmodo.ditest

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import ru.gizmodo.ditest.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private val viewModel: FirstViewModel by lazy {
        getViewModel { FirstViewModel() }
    }

    private val TAG: String = "Gizmodo"

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

    private fun moveFocus(view: EditText) {
        view.requestFocus()
    }

    fun Activity.hideSoftKeyboardExt() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        currentFocus?.apply {
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
        }
    }

    fun Fragment.hideSoftKeyboardExt() {
        activity?.hideSoftKeyboardExt()
    }

    private fun hideKeyboard() {
        this.hideSoftKeyboardExt()
        /* val view = this.currentFocus
         if (view != null) {
             val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             hideMe.hideSoftInputFromWindow(view.windowToken, 0)
         }
         window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)*/
    }

    private fun onFocus(view: View?, hasFocus: Boolean) {
        if (hasFocus) (view as EditText).selectAll()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.edtCount.apply {
            showSoftInputOnFocus = false
            setSelectAllOnFocus(true)
            setOnClickListener { hideKeyboard() }
            setOnFocusChangeListener { _, _ -> hideKeyboard() }
//            setOnFocusChangeListener { view, hasFocus -> onFocus(view, hasFocus) }
        }

        binding.edtBarcode.apply {
            showSoftInputOnFocus = false
            setSelectAllOnFocus(true)
            setOnClickListener { hideKeyboard() }
            setOnFocusChangeListener { _, _ -> hideKeyboard() }
//            setOnFocusChangeListener { view, hasFocus -> onFocus(view, hasFocus) }
        }

        binding.buttonFirst.isFocusable = false

        binding.edtBarcode.setOnKeyListener { _: View, i: Int, keyEvent: KeyEvent ->
            if (i == 66 && keyEvent.action == KeyEvent.ACTION_UP) {
                Log.d(TAG, "Move focus to count")
                moveFocus(binding.edtCount)
                onFocus(binding.edtCount, true)
            }
            !true
        }
        binding.edtCount.setOnKeyListener { _: View, i: Int, keyEvent: KeyEvent ->
            if (i == 66&& keyEvent.action == KeyEvent.ACTION_UP) {
                Log.d(TAG, "Move focus to barcode")
                moveFocus(binding.edtBarcode)
                onFocus(binding.edtBarcode, true)
            }
            true
        }

        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        viewModel.urovoScannerLiveData.observe(viewLifecycleOwner) { it ->
            binding.textviewFirst.text = it
            Log.d(TAG, it)
        }

        viewModel.superScannerLiveData.observe(viewLifecycleOwner) { it: SuperResults ->
            Log.d(TAG, "Значения со слушателей: " + it.toString())
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