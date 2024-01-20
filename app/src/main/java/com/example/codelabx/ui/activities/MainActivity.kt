package com.example.codelabx.ui.activities

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.codelabx.R
import com.example.codelabx.adapters.FilesAdapter
import com.example.codelabx.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var spinner : Spinner
    lateinit var codeEditor : EditText
    lateinit var filesRV : RecyclerView
    lateinit var curDirName : TextView

    lateinit var viewModel: MainViewModel
    lateinit var filesAdapter: FilesAdapter
    private  val TAG = "ABHI"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createView()
        checkStoragePermission()
        createViewModel()
        setupFiles()



    }

    private fun setupFiles() {
        curDirName.text = viewModel.getCurDirectoryName()

        viewModel.getAllFilesFromCurDirectory()
        Log.d(TAG, "onCreate: ${viewModel.getCurDirectoryName()}")
        viewModel.files.observe(this , Observer {

            filesAdapter = FilesAdapter()
            filesAdapter.submitList(it)
            filesRV.adapter = filesAdapter
            filesRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false)

        })
    }

    private fun checkStoragePermission(){
       if (ContextCompat.checkSelfPermission(this , Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
           ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
           askStoragePermissions()
       }
    }

    private fun askStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            AlertDialog.Builder(this)
                .setTitle("Storage Permission Required")
                .setMessage("Storage permission is required to manage the file.")
                .setCancelable(false)
                .setPositiveButton("Grant Permission", DialogInterface.OnClickListener { dialogInterface, i ->
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE) , 200)
                })
                .create().show()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE) , 200)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 200){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted...", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Permission Denied!!!", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun createViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun createView() {
        spinner = findViewById(R.id.languages_spinner)
        codeEditor = findViewById(R.id.code_editor)
        filesRV = findViewById(R.id.files_recycler_view)
        curDirName = findViewById(R.id.cur_working_dir)

        setSpinner()
    }

    private fun setSpinner() {
        val adapter = ArrayAdapter.createFromResource(this, R.array.languages, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}