package cc.iceq.rss.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import cc.iceq.rss.R
import cc.iceq.rss.databinding.RssListFragmentBinding
import cc.iceq.rss.service.ArticleServiceImpl


class RssListFragment : Fragment() {

    companion object {
        fun newInstance() = RssListFragment()
    }

    private var _binding: RssListFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel =
            ViewModelProvider(this).get(MainViewModel::class.java)
        val inflate = RssListFragmentBinding.inflate(inflater, container, false)
        _binding = inflate
        val root: View = binding.root
        return root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

}