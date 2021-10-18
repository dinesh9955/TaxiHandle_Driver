package com.rydz.driver.view.fragment

import com.rydz.driver.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.apiConstants.Urls
import com.rydz.driver.view.activity.MainActivity

import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class TermsConditionFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_about_us, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.terms_condition)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)

        webview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url!!)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                try
                {
                    progress.visibility=View.GONE
                }catch (ex:Exception)
                {
                    ex.printStackTrace()
                }
            }
        }
        webview!!.getSettings().setJavaScriptEnabled(true);
        webview!!.getSettings().setDomStorageEnabled(true);
        webview!!.loadUrl(Urls.TERM_CONDITION_URL)
    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(SettingFragment(),AppConstants.NOMAINSCREEN)
            }

        }
    }
}
