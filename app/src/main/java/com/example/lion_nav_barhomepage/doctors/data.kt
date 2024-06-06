package com.example.lion_nav_barhomepage.doctors

data class data(
    var id: Int ?=null,
    var name:String?=null,
    var spec: String?=null,
    var about:String?=null,
    var img_url:String?=null,
    var exp:String?=null,
    var avl: ArrayList<Int>?=null,
    var timeslots: List<List<Int>>?=null,
    var lang: String?=null,
    var fee: String?=null
)
//var doc_01 = data(1,"name1",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//
//    listOf(1,2,3),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_02 = data(2,"Harshini",
//    "spec2",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2),
//    listOf(listOf(1,1,1), listOf(0,0,1), listOf(0,0,0), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_03 = data(1,"name2",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//   listOf(2,3),
//    listOf(listOf(0,0,0), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_04 = data(1,"name3",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,0), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_05 = data(1,"name4",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2,3),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_06 = data(1,"name4",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(2,3,5),
//    listOf(listOf(0,0,0), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,1), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_07 = data(1,"name5",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2,3),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_08 = data(1,"name6",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2,3),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_09 = data(1,"name7",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(1,2,3),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(0,0,0),listOf(0,0,0), listOf(0,0,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//var doc_10 = data(1,"name8",
//    "spec1",
//    "mbbs md",
//    "https://imagesx.practo.com/providers/dr-vasudev-general-physician-visakhapatnam-a4c05254-890c-4ebc-a4d4-1b9cb14f97dc.jpg?i_type=t_100x100-2x",
//    "7 years",
//    listOf(4,6),
//    listOf(listOf(1,1,1), listOf(1,0,1), listOf(0,0,1), listOf(1,0,0),listOf(0,0,0), listOf(0,1,0), listOf(0,0,0)),
//    "English,Telugu,Hindi")
//
//
////class doctors{
//   // fun doc_list():List<data>{
//      //  return listOf(doc_01,doc_02,doc_03,doc_04,doc_05,doc_06,doc_07,doc_08,doc_09,doc_10)
//  //  }
//
//}