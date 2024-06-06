package com.example.lion_nav_barhomepage.doctors

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DoctorsViewModel: ViewModel() {
    private val _doctors =  MutableLiveData<ArrayList<data>>()
    val doctors: LiveData<ArrayList<data>> = _doctors
    private val _pos =  MutableLiveData<Int>()
    val pos: LiveData<Int> = _pos
    // private val _avlwords =  MutableList<String>(7,index->"1"+index)

    var text : String = ""

    fun setposition(position: Int){
        _pos.value=position

//        Log.e("on","${doctors.value?.get(position)}")
//     Log.e("print:::::","${get_data()}")
    }
    fun set_data(){
        _doctors.value= DoctorList

    }

    fun get_data(): data?
    {
        if(_pos.value == -1){
            return null
        }
        return _pos.value?.let { _doctors.value?.get(it )}
    }
    fun setavl(avl:List<Int>) {
        text=""
        for (i in avl){
            when(i){
                1->text+="Sun"
                2->text+=" Mon"
                3-> text+=" Tue"
                4->text+=" Wed"
                5->text+=" Thur"
                6->text+=" Fri"
                7->text+=" Sat"
            }

        }

    }

}
