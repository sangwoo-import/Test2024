package com.example.mytest2024

import android.Manifest
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.drawable.ColorDrawable
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.mytest2024.swaggerapi.LoginUserInformation
import com.example.mytest2024.swaggerapi.Retrofit.LogoutRequestData
import com.example.mytest2024.databinding.MyFragmentBinding
import com.example.mytest2024.swaggerapi.swaggercontroller.LogoutProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyFragment : Fragment(), LogoutProvider.CallBack {

    private var currentPhotoPath: String? = null // 촬영한 사진 경로 저장

    private lateinit var binding: MyFragmentBinding
    private val logoutProvider = LogoutProvider(this@MyFragment)
    private var codeA: String = ""
    private var messageA: String = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MyFragmentBinding.inflate(inflater)


        // 내부 저장소에서 이미지 불러오기 (앱이 다시 실행될 때 유지)
        loadImageFromInternalStorage()

        binding.username.text = LoginUserInformation.userName
        binding.position.text = LoginUserInformation.className
        binding.wardNm.text = LoginUserInformation.wardName


        /*로그아웃 cardview 또는 화살표 */
        binding.logoutImageButton.setOnClickListener {
            dialogMessageBox()
        }



        /* 갤러리 불러 및 사진 촬영 기능 전 다이얼로그 표시 */
        binding.CameraImageButton.setOnClickListener {
            showBottomDialog()
        }




        return binding.root // root는 contraint 전체를 뜻한다
    }


    /* dialog: 현재 AlertDialog를 조작할 수 있는 객체. 닫거나 다른 작업을 수행할 때 사용합니다.
        which: 클릭된 버튼을 구분하는 정수 값. 어떤 버튼이 눌렸는지 확인할 때 유용합니다 (BUTTON_POSITIVE, BUTTON_NEGATIVE, BUTTON_NEUTRAL 중 하나). */

    fun dialogMessageBox() {

        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("로그아웃 하시겠습니까?")
            .setPositiveButton("네") { dial, which ->

                val userSn = LoginUserInformation.userSn
                val logoutRequestDataSet = LogoutRequestData(
                    "${userSn}"
                )
                requestLogout(logoutRequestDataSet)
                dial.dismiss()

            }
            .setNegativeButton("아니요") { dial, which ->
                dial.dismiss()
            }

        dialog.show()


    }


    /*카메라 권한*/

    private val CAMERA_PERMISSION_REQUEST_CODE = 101

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {

            // 권한 요청
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            takePhoto() // 이미 권한이 있으면 바로 실행
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto() // 권한이 허용되면 사진 촬영 실행
            } else {
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /*사진 90도 180도 270도 일때 정면 보기*/
    fun rotateImageIfRequired(photoPath: String, bitmap: Bitmap): Bitmap {
        val exif = ExifInterface(photoPath)
        val orientation =
            exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)

        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90f)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180f)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270f)
            else -> bitmap
        }
    }

    private fun rotateBitmap(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }


    /* 사진 촬영 */
    fun takePhoto() {
        val photoFile: File? = createImageFile()
        if (photoFile != null) {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireContext(),
                "com.example.mytest2024.fileprovider",
                photoFile
            )
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //  이거 추가!
            startActivityForResult(intent, 200)
        }

    }

    /*  파일 생성 (사진 촬영 시 저장할 파일 생성) */
    private fun createImageFile(): File? {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }


    /* 갤러리*/

    //  갤러리 열기
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 100)
    }

    //  갤러리에서 선택한 이미지를 내부 저장소에 저장하는 함수
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                100 -> { // 갤러리 선택
                    val imageUri = data?.data
                    if (imageUri != null) {
                        val savedPath = saveImageToInternalStorage(imageUri)
                        Log.d("ImagePath", "Saved at: $savedPath")
                        loadImageFromInternalStorage()
                    }
                }

                200 -> { // 카메라 촬영
                    if (currentPhotoPath != null) {
                        val savedPath = saveCapturedImageToInternalStorage()
                        Log.d("ImagePath", "Captured image saved at: $savedPath")
                        loadImageFromInternalStorage()
                    }
                }
            }
        }
    }


    /*  카메라로 촬영한 이미지 내부 저장소에 복사 저장 */
    private fun saveCapturedImageToInternalStorage(): String {
        val bitmap = MediaStore.Images.Media.getBitmap(
            requireActivity().contentResolver,
            Uri.fromFile(File(currentPhotoPath))
        )
        val rotatedBitmap = rotateImageIfRequired(currentPhotoPath!!, bitmap) // 사진 정면 정렬


        val file = File(requireActivity().filesDir, "profile_image.jpg")
        val outputStream = FileOutputStream(file)
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream) // 정렬로 저장
        outputStream.close()

        // 파일 경로를 SharedPreferences에 저장
        val sharedPref = requireActivity().getSharedPreferences("MyGallery", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_image_path", file.absolutePath)
            apply()
        }

        return file.absolutePath
    }


    //  선택한 갤러리  내부 저장소에 복사하는 함수
    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        val file = File(requireActivity().filesDir, "profile_image.jpg") // 내부 저장소 경로
        val outputStream = file.outputStream()

        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()

        // 파일 경로를 SharedPreferences에 저장
        val sharedPref = requireActivity().getSharedPreferences("MyGallery", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("profile_image_path", file.absolutePath)
            apply()
        }

        return file.absolutePath
    }


    //  내부 저장소에서 이미지 불러와서 ImageView에 표시
    private fun loadImageFromInternalStorage() {
        val sharedPref = requireActivity().getSharedPreferences("MyGallery", Context.MODE_PRIVATE)
        val imagePath = sharedPref.getString("profile_image_path", null)

        if (imagePath != null) {
            val file = File(imagePath)
            if (file.exists()) {  // 내부 저장소의 파일 로드
                Glide.with(this).load(file).circleCrop()  // 이미지가 원으로 변한다
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // 디스크 캐시 사용 안 함,(다른 이미지 변경할 때 안바껴서)
                    .skipMemoryCache(true) // 메모리 캐시도 사용 안 함
                    .into(binding.myPageProfileImageview)
            }
        }
    }


    /* bottom sheet dialog */

    fun showBottomDialog() {

        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottom_sheet_dialog)

        val photoLayout = dialog.findViewById<LinearLayout>(R.id.layoutCamera)
        val galleryLayout = dialog.findViewById<LinearLayout>(R.id.layoutGallery)
        val cancelBtn = dialog.findViewById<ImageView>(R.id.dissMissBtn)
        val returnDefaultUserImage = dialog.findViewById<LinearLayout>(R.id.layoutBasicUserImage)


        /*카메라*/
        photoLayout.setOnClickListener {
            checkCameraPermission()
            dialog.dismiss()
        }

        /*갤러리*/
        galleryLayout.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }

        /* 기본 이미지로 변경 */
        returnDefaultUserImage.setOnClickListener {
            // 이미지 Source 를 원래 이미지 소스로 변경 시킨다.
            binding.myPageProfileImageview.setImageResource(R.drawable.baseline_person_24)
            val sharedPref = requireActivity().getSharedPreferences("MyGallery", Context.MODE_PRIVATE)
            with(sharedPref.edit()) {
                // 기본 이미지도 앱 에서 유지 시킬려고
                putString("profile_image_path", null)
                apply()
            }

            dialog.dismiss()
        }

        /*취소 버튼*/
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 천천히 올라오는 Animation 적용
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.window?.setGravity(Gravity.BOTTOM)


    }


    /* 로그아웃 Retrofit*/

    fun requestLogout(logoutRequestDataSet: LogoutRequestData) {
        logoutProvider.sendLogout(logoutRequestDataSet)
    }


    override fun completeLogout(code: String, msg: String) {
        codeA = code
        messageA = msg
        if (codeA.equals("100")) {
            Toast.makeText(requireContext(), messageA + "했습니다!", Toast.LENGTH_SHORT)
                .show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.apply {
                startActivity(intent)
                requireActivity().finish()
            }
        } else if (codeA.equals("E100")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT)
                .show()
        } else if (codeA.equals("E600")) {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT)
                .show()
        } else {
            Log.d("error!!", codeA + ": " + messageA)
            Toast.makeText(requireContext(), messageA, Toast.LENGTH_SHORT)
                .show()
        }


    }


}