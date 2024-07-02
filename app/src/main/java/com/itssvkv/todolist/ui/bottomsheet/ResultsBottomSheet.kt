package com.itssvkv.todolist.ui.bottomsheet

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.itssvkv.todolist.R
import com.itssvkv.todolist.databinding.BottomSheetResultsBinding
import com.itssvkv.todolist.utils.CommonFunctions
import com.itssvkv.todolist.utils.Constants.BICYCLE
import com.itssvkv.todolist.utils.Constants.DOUBLE_LEG
import com.itssvkv.todolist.utils.Constants.JUMPING_ROPE
import com.itssvkv.todolist.utils.Constants.LAYING_MOANING
import com.itssvkv.todolist.utils.Constants.SITTING_POSE
import com.itssvkv.todolist.utils.Constants.STANDING
import com.itssvkv.todolist.utils.Constants.STANDING_FROM_SET
import com.itssvkv.todolist.utils.Constants.SWIMMING
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResultsBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetResultsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var bundle: Bundle

    private val resultsBottomSheetViewModel by viewModels<ResultsBottomSheetViewModel>()

    init {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetResultsBinding.inflate(inflater, container, false)
        val responseValue = bundle.getInt("results")
        responseCases(response = responseValue)
        init()
        return binding.root
    }


    private fun init() {
        initClicks()
        initObservers()
        afterUploadCases()

    }

    private fun initClicks() {
        binding.cancelIv.setOnClickListener {
            this@ResultsBottomSheet.dismiss()
        }

        binding.saveButton.setOnClickListener {
            uploadToTheDiagnosisToFirebase()
        }
    }

    private fun uploadToTheDiagnosisToFirebase() {
        CommonFunctions.buttonState(state = false, binding.saveButton, requireContext())
        resultsBottomSheetViewModel.updateCurrentUserInfo(
            diagnosis = binding.firstDiscTv.text.toString()
        )
    }

    private fun initObservers() {
        resultsBottomSheetViewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {

                CommonFunctions.progressBarState(
                    state = true,
                    binding.progressBar
                )
                binding.saveButton.text = ""
            } else {
                CommonFunctions.progressBarState(
                    state = false,
                    binding.progressBar
                )
                binding.saveButton.text = resources.getString(R.string.save)
            }
        }

    }


    private fun afterUploadCases() {
        resultsBottomSheetViewModel.isUploadedSuccessfully = {
            Toast.makeText(requireContext(), "uploaded successfully", Toast.LENGTH_SHORT).show()
            this@ResultsBottomSheet.dismiss()
        }

        resultsBottomSheetViewModel.isUploadedFailure = {
            Toast.makeText(requireContext(), "there is some error about $it", Toast.LENGTH_LONG)
                .show()
        }
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


    private fun initBrowserIntent(uri: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW)
        browserIntent.setData(Uri.parse(uri))
        browserIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(browserIntent)
    }


    private fun initExercise(response: Int) {
        binding.firstExerciseLinkTv.setOnClickListener {
            animateView(it)
            when (response) {
                0 -> {
                    initBrowserIntent(uri = DOUBLE_LEG)
                }

                1 -> {
                    initBrowserIntent(uri = DOUBLE_LEG)
                }

                2 -> {
                    initBrowserIntent(uri = BICYCLE)
                }

                3 -> {
                    initBrowserIntent(uri = SWIMMING)
                }

                4 -> {
                    initBrowserIntent(uri = LAYING_MOANING)
                }
            }
        }
        binding.secExerciseLinkTv.setOnClickListener {
            animateView(it)
            when (response) {
                0 -> {
                    initBrowserIntent(uri = SITTING_POSE)
                }

                1 -> {
                    initBrowserIntent(uri = SITTING_POSE)

                }

                2 -> {
                    initBrowserIntent(uri = JUMPING_ROPE)
                }

                3 -> {
                    initBrowserIntent(uri = STANDING)
                }

                4 -> {
                    initBrowserIntent(uri = STANDING_FROM_SET)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun responseCases(response: Int) {

        when (response) {
            0 -> {
                binding.firstDiscTv.text = "Mild Osteoarthritis"
                binding.secDiscTv.text =
                    "Low-impact activities like swimming \nand cycling to maintain joint \nmobility and strengthen muscles."
                binding.thirdDiscTv.text = "Reducing stress on the knee \njoint."
                binding.fourthDiscTv.text = "Strengthening exercises and flexibility \ntraining."
                binding.fiveDiscTv.text =
                    "Use of heat or cold therapy \nand over-the-counter pain medications \nas needed."
                binding.firstExerciseTv.text =
                    resources.getString(R.string.first_exercise, "Quadriceps Strengthening:")
                binding.firstExerciseLinkTv.text =
                    resources.getString(R.string.first_exercise_link, "Straight Leg Raises")
                binding.firstRepetitionsTv.text =
                    resources.getString(R.string.first_repetitions, "3 sets of 10-15 reps per leg.")
                binding.secExerciseTv.text =
                    resources.getString(R.string.sec_exercise, "Hamstring Stretching:")
                binding.secExerciseLinkTv.text =
                    resources.getString(R.string.sec_exercise_link, "Seated Hamstring Stretch")
                binding.secRepetitionsTv.text =
                    resources.getString(R.string.sec_hold, "20-30 seconds, repeat 3 times per leg.")
            }

            1 -> {
                binding.firstDiscTv.text = "Mild Osteoarthritis"
                binding.secDiscTv.text =
                    "Low-impact activities like \nswimming and cycling to maintain \njoint mobility and strengthen \nmuscles."
                binding.thirdDiscTv.text = "Reducing stress on the knee \njoint."
                binding.fourthDiscTv.text = "Strengthening exercises and flexibility \ntraining."
                binding.fiveDiscTv.text =
                    "Use of heat or cold therapy \nand over-the-counter pain \nmedications as needed."
                binding.firstExerciseTv.text =
                    resources.getString(R.string.first_exercise, "Quadriceps Strengthening:")
                binding.firstExerciseLinkTv.text =
                    resources.getString(R.string.first_exercise_link, "Straight Leg Raises")
                binding.firstRepetitionsTv.text =
                    resources.getString(R.string.first_repetitions, "3 sets of 10-15 reps per leg.")
                binding.secExerciseTv.text =
                    resources.getString(R.string.sec_exercise, "Hamstring Stretching:")
                binding.secExerciseLinkTv.text =
                    resources.getString(R.string.sec_exercise_link, "Seated Hamstring Stretch")
                binding.secRepetitionsTv.text =
                    resources.getString(R.string.sec_hold, "20-30 seconds, repeat 3 times per leg.")
            }

            2 -> {
                binding.firstDiscTv.text = "Mild to Moderate Osteoarthritis"
                binding.secDiscTv.text =
                    "Low-impact activities like swimming \nand cycling to maintain joint mobility and \nstrengthen muscles."
                binding.thirdDiscTv.text = "Reducing stress on the knee \njoint."
                binding.fourthDiscTv.text = "More focused on pain relief and \nimproving function."
                binding.fiveTitleTv.text = "Orthotics:"
                binding.fiveDiscTv.text = "Shoe inserts to improve joint \nalignment."
                binding.firstExerciseTv.text =
                    resources.getString(R.string.first_exercise, "Low-Impact Aerobic Exercise:")
                binding.firstExerciseLinkTv.text =
                    resources.getString(R.string.first_exercise_link, "Stationary Cycling")
                //bycicl
                binding.firstRepetitionsTv.text = resources.getString(
                    R.string.first_repetitions,
                    "20-30 minutes, 3-5 times per week."
                )
                binding.secExerciseTv.text =
                    resources.getString(R.string.sec_exercise, "Hip Abductor Strengthening:")
                binding.secExerciseLinkTv.text =
                    resources.getString(R.string.sec_exercise_link, "Side-Lying Leg Lifts")
                binding.secRepetitionsTv.text =
                    resources.getString(R.string.sec_hold, "3 sets of 10-15 reps per leg.")
            }


            3 -> {
                binding.firstDiscTv.text = "Moderate Osteoarthritis"
                binding.secTitleTv.text = "Medications:"
                binding.secDiscTv.text =
                    "Prescription pain relievers and anti-inflammatory \ndrugs."
                binding.thirdTitleTv.text = "Injections:"
                binding.thirdDiscTv.text =
                    "Corticosteroid or hyaluronic acid injections \nfor pain relief."
                binding.fourthDiscTv.text =
                    "Including manual therapy and more intensive \nexercise regimens."
                binding.fiveTitleTv.text = "Assistive Devices:"
                binding.fiveDiscTv.text = "Canes or walkers to reduce joint \nstrain."
                binding.firstExerciseTv.text =
                    resources.getString(R.string.first_exercise, "Water-Based Exercise:")
                binding.firstExerciseLinkTv.text =
                    resources.getString(R.string.first_exercise_link, "Swimming")
                binding.firstRepetitionsTv.text =
                    resources.getString(R.string.first_repetitions, "30 minutes, 3 times per week.")
                binding.secExerciseTv.text =
                    resources.getString(R.string.sec_exercise, "Balance and Stability Exercises:")
                binding.secExerciseLinkTv.text =
                    resources.getString(R.string.sec_exercise_link, "Single Leg Stands")
                //standeg
                binding.secRepetitionsTv.text =
                    resources.getString(R.string.sec_hold, "3 sets per leg.")
            }

            4 -> {
                binding.firstDiscTv.text = "Severe Osteoarthritis"
                binding.secDiscTv.text =
                    "Low-impact activities like swimming \n*and cycling to maintain joint mobility and \nstrengthen muscles."
                binding.thirdTitleTv.text = "Surgical Considerations:"
                binding.thirdDiscTv.text =
                    "Arthroscopy, osteotomy, or knee replacement may \nbe necessary."
                binding.fourthDiscTv.text =
                    " Including manual therapy and more intensive \nexercise regimens."
                binding.fiveTitleTv.text = "Pain Management:"
                binding.fiveDiscTv.text = "Stronger medications and possibly regular injections."
                binding.firstExerciseTv.text =
                    resources.getString(R.string.first_exercise, "Range of Motion Exercises:")
                binding.firstExerciseLinkTv.text =
                    resources.getString(R.string.first_exercise_link, "Heel Slides")
                binding.firstRepetitionsTv.text =
                    resources.getString(R.string.first_repetitions, "3 sets of 10-15 reps per leg.")
                binding.secExerciseTv.text =
                    resources.getString(R.string.sec_exercise, "Functional Training:")
                binding.secExerciseLinkTv.text =
                    resources.getString(R.string.sec_exercise_link, "Sit-to-Stand")
                //sfs
                binding.secRepetitionsTv.text =
                    resources.getString(R.string.sec_hold, "3 sets of 10-15 reps.")
            }
        }
        initExercise(response = response)
    }
}

