package dk.itu.bachelor.voyager.fragments

import android.R
import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import dk.itu.bachelor.voyager.activities.MainActivity
import dk.itu.bachelor.voyager.databinding.FragmentPlanYourTripBinding


class PlanYourTripFragment : Fragment() {
        /**
         * View binding is a feature that allows you to more easily write code that interacts with
         * views. Once view binding is enabled in a module, it generates a binding class for each XML
         * layout file present in that module. An instance of a binding class contains direct references
         * to all views that have an ID in the corresponding layout.
         */
        private var _binding: FragmentPlanYourTripBinding ? = null


        /**
         * This property is only valid between `onCreateView()` and `onDestroyView()` methods.
         */
        private val binding get() = _binding!!


        @SuppressLint("SetJavaScriptEnabled")
        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            // Inflate the layout for this fragment
            _binding = FragmentPlanYourTripBinding.inflate(inflater, container, false)

            @ColorInt val colorPrimaryLight =
                ContextCompat.getColor(requireContext(), R.color.holo_orange_light)


            val url = "https://www.buildai.space/app/voyager-app"

            //Custom tabs to load the AI
            val customTabsIntent = CustomTabsIntent.Builder() // set the default color scheme
                .setDefaultColorSchemeParams(
                    CustomTabColorSchemeParams.Builder()
                        .setToolbarColor(colorPrimaryLight)
                        .build()
                )
                .setStartAnimations(requireContext(), R.anim.slide_in_left, R.anim.slide_out_right)
                .setExitAnimations(requireContext(), R.anim.slide_in_left, R.anim.slide_out_right)
                .setUrlBarHidingEnabled(true)
                .setShowTitle(true)
                .build()

            customTabsIntent.launchUrl(requireContext(), Uri.parse(url))

            binding.launchTripPlanner.setOnClickListener{
                customTabsIntent.launchUrl(requireContext(), Uri.parse(url))
            }

            /*
            //Webview to load the AI
            val webView = binding.webView
            webView.settings.javaScriptEnabled = true
            webView.settings.useWideViewPort = true
            webView.setWebViewClient(WebViewClient()) //open urls inside browser


            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            //webView.setWebChromeClient(WebChromeClient())


            // load the URL you want to redirect to in the WebView
            webView.loadUrl("https://www.buildai.space/app/voyager-app")
            //webView.loadUrl("https://www.vg.no")
            //webView.loadUrl("https://www.buildai.space/app/dae3da25-888e-448f-b15c-5a20ca4ca961")*/

            return binding.root
        }

        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

}