package com.itssvkv.todolist.ui.homescreen

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.FragmentHomeBinding
import com.itssvkv.todolist.model.UserDetails
import com.itssvkv.todolist.ui.bottomsheet.ResultsBottomSheet
import com.itssvkv.todolist.utils.CommonFunctions
import com.itssvkv.todolist.utils.Constants
import com.itssvkv.todolist.utils.Constants.BANG_LOUDLY
import com.itssvkv.todolist.utils.Constants.INABILITY
import com.itssvkv.todolist.utils.Constants.REDNESS
import com.itssvkv.todolist.utils.Constants.TAG
import com.itssvkv.todolist.utils.Constants.WEAKNESS
import com.itssvkv.todolist.utils.NewTfLiteManager
import com.itssvkv.todolist.utils.NewTfLiteManager.convertBitmapToByteBuffer
import com.itssvkv.todolist.utils.NewTfLiteManager.loadImageFromUri
import com.itssvkv.todolist.utils.NewTfLiteManager.preprocessImage
import com.itssvkv.todolist.utils.NewTfLiteManager.runInference
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.nio.ByteBuffer
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel by viewModels<HomeViewModel>()

    private var imgResult: Uri? = null
    private var resultLauncher: ActivityResultLauncher<Intent>? = null
    private val imgIntent by lazy { Intent() }
    private var currentUserInfo: UserDetails? = null

    private var resultsBottomSheet: ResultsBottomSheet? = null

    private var imageUploadSuccessful: (() -> Unit)? = null
    private var imageUploadFailed: ((String) -> Unit)? = null

    @Inject
    lateinit var bundle: Bundle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        CommonFunctions.buttonState(
            state = false,
            view = binding.checkResultButton,
            context = requireContext()
        )
        NewTfLiteManager.loadModel(context = requireContext())
        init()
        return binding.root
    }


    private fun init() {
        getUserInfo()
        initClicks()
        initObservers()
        initStateCheckResultBTN()
        selectImage()
        isImageUploaded()
        initBrowserIntentClicks()
    }

    private fun animateView(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.2f, 1.0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.2f, 1.0f)
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.5f, 1.0f)

        val animator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY, alpha)
        animator.duration = 1000
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }


    private fun initBottomSheets() {
        resultsBottomSheet = ResultsBottomSheet()
        resultsBottomSheet?.show(parentFragmentManager, "test")

    }

    private fun getUserInfo() {
        homeViewModel.currentUserInfo.observe(viewLifecycleOwner) { userInfo ->
            currentUserInfo = userInfo
            setUserInfo()

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUserInfo() {
        Glide.with(binding.profileIv.context).load(currentUserInfo?.photo)
            .apply(RequestOptions().dontTransform())
            .into(binding.profileIv)
        binding.nameTv.text = "Hi, ${currentUserInfo?.name}"
    }

    private fun initClicks() {

        binding.cameraOnPhotoIv.setOnClickListener {
            animateView(it)
            resultLauncher?.launch(imgIntent)
        }

        binding.checkResultButton.setOnClickListener {
            CommonFunctions.buttonState(state = false, view = it, context = requireContext())
            homeViewModel.checkResult()
            initBottomSheets()
        }

        binding.profileIv.setOnClickListener {
            this@HomeFragment.findNavController()
                .navigate(R.id.action_homeFragment_to_profileFragment)
        }
    }


    private fun initStateCheckResultBTN() {
        homeViewModel.checkResultSuccessful = {
            Log.d(TAG, "toasts: done")
            CommonFunctions.progressBarState(state = true, binding.progressBar)
            CommonFunctions.buttonState(state = true, binding.checkResultButton, requireContext())

        }
        homeViewModel.checkResultFailed = {
            CommonFunctions.progressBarState(state = false, binding.progressBar)
            CommonFunctions.buttonState(state = true, binding.checkResultButton, requireContext())
            Toast.makeText(requireContext(), "$it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initObservers() {
        homeViewModel.isCheckResult.observe(viewLifecycleOwner) {
            if (it) {
                CommonFunctions.progressBarState(true, binding.progressBar)
                binding.checkResultButton.text = ""
            } else {
                CommonFunctions.progressBarState(false, binding.progressBar)
                binding.checkResultButton.text = resources.getString(R.string.check_result)
            }
        }

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                CommonFunctions.progressBarState(true, binding.intentProgressBar)
                binding.onPhotoTv.text = ""
                binding.onPhotoTv2.text = ""
                binding.cameraOnPhotoIv.visibility = View.INVISIBLE

            } else {
                CommonFunctions.progressBarState(false, binding.intentProgressBar)
                binding.cameraOnPhotoIv.visibility = View.VISIBLE
            }
        }
    }

    private fun selectImage() {
        imgIntent.apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    imgResult = it?.data?.data
                    Log.d("itssvkv", "selectImage: $imgResult")
                    imgResult?.let { imgUri ->
                        imageUploadSuccessful?.invoke()
                        try {
                            val bitmap: Bitmap = loadImageFromUri(imgUri, requireContext())
                            val preprocessedBitmap: Bitmap = preprocessImage(bitmap)
                            val byteBuffer: ByteBuffer =
                                convertBitmapToByteBuffer(preprocessedBitmap)
                            val result: Int = runInference(byteBuffer)
                            bundle.putInt("results", result)
                            Log.d(TAG, "selectImage: $result")
                            // Process the result
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                } else {
                    imageUploadFailed?.invoke("error")
                }
            }
    }

    private fun isImageUploaded() {
        imageUploadSuccessful = {
            binding.onPhotoTv.text = resources.getString(R.string.image_uploaded)
            binding.onPhotoTv2.text = resources.getString(R.string.please_click_check_result)
            CommonFunctions.buttonState(
                state = true,
                view = binding.checkResultButton,
                context = requireContext()
            )
            binding.cameraOnPhotoIv.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.done,
                    resources.newTheme()
                )
            )
        }

        imageUploadFailed = {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            binding.onPhotoTv.text = resources.getString(R.string.check_knee_test_result)
            binding.onPhotoTv2.text = resources.getString(R.string.please_insert_x_ray_picture)
            CommonFunctions.buttonState(
                state = false,
                view = binding.checkResultButton,
                context = requireContext()
            )
            binding.cameraOnPhotoIv.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources, R.drawable.camera,
                    resources.newTheme()
                )
            )
        }

    }

    private fun initBrowserIntentClicks() {
        binding.knee1Iv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = Constants.SWELLING)
        }
        binding.knee1Tv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = Constants.SWELLING)
        }
        binding.knee2Iv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = REDNESS)
        }
        binding.knee2Tv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = REDNESS)
        }
        binding.knee3Iv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = WEAKNESS)
        }
        binding.knee3Tv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = WEAKNESS)
        }
        binding.knee4Iv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = BANG_LOUDLY)
        }
        binding.knee4Tv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = BANG_LOUDLY)
        }

        binding.knee5Iv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = INABILITY)
        }
        binding.knee5Tv.setOnClickListener {
            animateView(it)
            initBrowserIntent(uri = INABILITY)
        }

    }

    private fun initBrowserIntent(uri: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.setData(Uri.parse(uri))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(browserIntent)
    }
}