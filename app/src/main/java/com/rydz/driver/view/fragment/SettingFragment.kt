package com.rydz.driver.view.fragment

import android.content.Intent
import android.net.Uri
import com.rydz.driver.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rydz.driver.CommonUtils.AppConstants
import com.rydz.driver.apiConstants.Urls
import com.rydz.driver.view.activity.MainActivity

import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.layout_common_toolbar.*

class SettingFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        initViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        MainActivity.mainActivity.onChangingfragment(AppConstants.NOMAINSCREEN)
        tv_title_common.text=getString(R.string.setting)
        iv_back_common.setImageResource(R.drawable.ic_back_black)
        iv_back_common.setOnClickListener(this)

        ll_privacy_policy.setOnClickListener(this)
        ll_about_us.setOnClickListener(this)
        ll_contact_us.setOnClickListener(this)
        ll_faq.setOnClickListener(this)
        ll_terms.setOnClickListener(this)
    }

    open fun initViews(view: View?) {

    }

    override fun onClick(v: View?) {
        super.onClick(v)
        when(v!!.id)
        {
            R.id.iv_back_common->
            {
                MainActivity.mainActivity.onReplaceFragment(AccountFragment(),AppConstants.ACCOUNTSCREEN)
            }

            R.id.ll_faq->
            {
             /*   val uris = Uri.parse(Urls.FAQ_URL)
                val intents = Intent(Intent.ACTION_VIEW, uris)
                startActivity(intents)*/
                MainActivity.mainActivity.onReplaceFragment(FAQFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_about_us->
            {
                MainActivity.mainActivity.onReplaceFragment(AboutUsFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_contact_us->
            {
                MainActivity.mainActivity.onReplaceFragment(ContactUsFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_privacy_policy->
            {
                MainActivity.mainActivity.onReplaceFragment(PrivacyPolicyFragment(),AppConstants.NOMAINSCREEN)
            }

            R.id.ll_terms->
            {
                MainActivity.mainActivity.onReplaceFragment(TermsConditionFragment(),AppConstants.NOMAINSCREEN)
            }



        }
    }
}
