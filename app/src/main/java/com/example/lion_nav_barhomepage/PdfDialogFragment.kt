package com.example.lion_nav_barhomepage

import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.example.lion_nav_barhomepage.databinding.FragmentPdfDialogBinding
import com.example.lion_nav_barhomepage.databinding.FragmentPdfactivityBinding
import com.github.barteksc.pdfviewer.PDFView
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PdfDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
var urlpdf : String = ""
lateinit var pdfView : PDFView
class PdfDialogFragment : DialogFragment() {


    private var _binding: FragmentPdfDialogBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPdfDialogBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        pdfView = binding.idPDFView
        PRDownloader.initialize(requireContext())
//        binding.progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        downloadPdfFromInternet(
            urlpdf,
            getRootDirPath(requireContext()),
            fileName
        )

    }

    fun seturl(url: String, f: Int) {
        urlpdf = url
        flag = f
    }

    fun getflag(): Int {
        return flag
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
    }

    private fun showPdfFromFile(file: File) {
        pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    requireContext(),
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    Toast.makeText(requireContext(), "downloadComplete", Toast.LENGTH_LONG)
                        .show()
                    val downloadedFile = File(dirPath, fileName)
//                    binding.progressBar.visibility = View.GONE
                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: Error?) {
                    Toast.makeText(
                        requireContext(),
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
    }
}

