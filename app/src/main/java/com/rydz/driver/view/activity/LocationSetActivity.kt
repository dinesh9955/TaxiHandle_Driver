package com.rydz.driver.view.activity


import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.rydz.driver.CommonUtils.PlacesEventObject
import com.rydz.driver.CommonUtils.locationMethods.OnLocationItemClickListener
import com.rydz.driver.R
import com.rydz.driver.model.socketResponse.Source
import com.rydz.driver.view.adapters.LocationBottomsheetAdapter
import kotlinx.android.synthetic.main.activity_location_set.*
import org.greenrobot.eventbus.EventBus
import rx.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit


/**
 *This class is used to slect source location and destination location
 */
class LocationSetActivity : BaseActivity(), OnLocationItemClickListener {


    lateinit var location: Location
    lateinit var apiKey: String

    lateinit var placesClient: PlacesClient
    lateinit var request: FindAutocompletePredictionsRequest
    lateinit var token: AutocompleteSessionToken

    lateinit var des_behaviorSubject: BehaviorSubject<String>
    lateinit var sourceplace: Source

    lateinit var destinationPlace: Source
    lateinit var autocompletePrediction: MutableList<AutocompletePrediction>
    lateinit var progress: ProgressBar
    var isSource: Boolean = false
    var isDestination: Boolean = false
    var countryCode: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_set)
        inits()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.iv_back -> {
                finish()

            }


        }
    }


    override fun onItemClick(name: String, place_obj: Source, des_obj: Source) {
        if (this!!.isSource) {
            sourceplace = place_obj
            destinationPlace = des_obj
            edt_destination.setText(sourceplace!!.name.toString())

            progress.visibility = View.GONE
            if (rv_location.adapter != null) {
                try {
                    initAutocompletePrediction()

                } catch (e: Exception) {

                }

                rv_location.visibility = View.GONE

            }
            /*********************************************/
            var placesEventObject: PlacesEventObject? = null
            if (intent.hasExtra("FROM")) {
                if (intent.getStringExtra("FROM").equals("SOURCE"))
                    placesEventObject = PlacesEventObject(sourceplace, destinationPlace, true, 0.0, true)
                else
                    placesEventObject = PlacesEventObject(sourceplace, destinationPlace, true, 0.0, false)
            }

            EventBus.getDefault().postSticky(placesEventObject)
            finish()
            /*********************************************/

        } else {
            sourceplace = place_obj
            destinationPlace = des_obj
            edt_destination.setText(destinationPlace!!.name.toString())

            progress.visibility = View.GONE
            if (rv_location.adapter != null) {
                Log.e("120", "120")
                initAutocompletePrediction()

                rv_location.visibility = View.GONE
            }

            /*********************************************/
            var placesEventObject: PlacesEventObject? = null
            if (intent.hasExtra("FROM")) {
                if (intent.getStringExtra("FROM").equals("SOURCE"))
                    placesEventObject = PlacesEventObject(sourceplace, destinationPlace, true, 0.0, true)
                else
                    placesEventObject = PlacesEventObject(sourceplace, destinationPlace, true, 0.0, false)
            }
            EventBus.getDefault().postSticky(placesEventObject)
            finish()
            /*********************************************/
        }
    }


    //intialization
    private fun inits() {

        progress = findViewById(R.id.progress)
        des_behaviorSubject = BehaviorSubject.create()

        if (intent != null && intent.hasExtra("COUNTRYCODE"))
            countryCode = intent?.getStringExtra("COUNTRYCODE")!!

        if (intent.hasExtra("FROM")) {
            if (intent.getStringExtra("FROM").equals("SOURCE"))
                isSource = true
            else
                isSource = false


        }

        des_behaviorSubject.debounce(100, TimeUnit.MILLISECONDS).onBackpressureLatest().subscribe {
            request = FindAutocompletePredictionsRequest.builder()


                /*.setCountry("USA")*/
                .setCountry(countryCode)
                .setSessionToken(token)
                .setQuery(it.toString())
                .build()
            // progress.visibility = View.VISIBLE
            placesClient.findAutocompletePredictions(request).addOnSuccessListener {
                progress.visibility = View.GONE
                // Log.e("182", it.autocompletePredictions.toString())
                if (it.autocompletePredictions != null && it.autocompletePredictions.size > 0) {

                    if (intent.hasExtra("FROM")) {
                        autocompletePrediction = it.autocompletePredictions
                        if (intent.getStringExtra("FROM").equals("SOURCE")) {
                            rv_location.adapter = LocationBottomsheetAdapter(
                                placesClient,
                                this@LocationSetActivity,
                                autocompletePrediction,
                                this@LocationSetActivity,
                                "source"
                            )
                        } else {
                            rv_location.adapter = LocationBottomsheetAdapter(
                                placesClient,
                                this@LocationSetActivity,
                                autocompletePrediction,
                                this@LocationSetActivity,
                                "destination"
                            )
                        }
                    }


                    // rv_location.adapter = LocationBottomsheetAdapter(placesClient, this@LocationSetActivity, autocompletePrediction, this@LocationSetActivity, "destination")
                } else {
                    if (rv_location.adapter != null) {
                        initAutocompletePrediction()

                    }
                }
            }.addOnCanceledListener {
                initAutocompletePrediction()

            }


        }


        //places api
        apiKey = getString(R.string.places_api_key)
        // Setup Places Client
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }
        placesClient = Places.createClient(this)
        token = AutocompleteSessionToken.newInstance();

        rv_location.layoutManager = LinearLayoutManager(this@LocationSetActivity) as RecyclerView.LayoutManager?

        if (EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java) != null) {
            destinationPlace = EventBus.getDefault().getStickyEvent(PlacesEventObject::class.java).destination

        } else {
        }



        edt_destination.setOnFocusChangeListener { v, hasFocus ->
            if (v.hasFocus()) {


                handleRecyclerView()

                if (edt_destination.text.toString().length == 0) {
                    //tv_pinloc.visibility = View.VISIBLE

                }
            }
        }


        //set destintaion text change
        edt_destination.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                des_behaviorSubject.onNext(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if (edt_destination.text.length == 0) {

                    if (rv_location.adapter != null) {

                        initAutocompletePrediction()


                    }


                } else {

                    Log.e("afterTextChanged ", "afterTextChanged")

                }
            }
        })




        edt_destination.setOnClickListener(View.OnClickListener {


            handleRecyclerView()


            if (edt_destination.isFocused() && edt_destination.getText().length > 0) {

                edt_destination.setSelection(edt_destination.getText().length, 0)
                //iv_cleardesc.visibility = View.VISIBLE

            } else {
                edt_destination.requestFocus()
                edt_destination.clearFocus()
                // iv_cleardesc.visibility = View.GONE

            }

        })


    }


    private fun handleRecyclerView() {

        initAutocompletePrediction()
        if (rv_location.adapter != null)
            rv_location.adapter!!.notifyDataSetChanged()

        rv_location.visibility = View.VISIBLE
    }


    private fun initAutocompletePrediction() {
        try {
            if (autocompletePrediction != null && autocompletePrediction.size>0) {

                autocompletePrediction.clear()



            }
            else
                autocompletePrediction = ArrayList<AutocompletePrediction>()
        } catch (e: UnsupportedOperationException) {
           e.printStackTrace()

            autocompletePrediction = ArrayList<AutocompletePrediction>()
        }catch (e: UninitializedPropertyAccessException) {
            autocompletePrediction = ArrayList<AutocompletePrediction>()
        }

        if (rv_location.adapter != null && autocompletePrediction.size==0 )
            rv_location.adapter=null
        else  if (rv_location.adapter != null )
            rv_location.adapter!!.notifyDataSetChanged()
           // rv_location.adapter!!.notifyDataSetChanged()


    }
}