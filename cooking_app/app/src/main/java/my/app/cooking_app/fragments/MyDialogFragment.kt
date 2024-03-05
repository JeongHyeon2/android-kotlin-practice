package my.app.cooking_app.fragments

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import my.app.cooking_app.R
import my.app.cooking_app.databinding.FragmentDialogBinding
import my.app.cooking_app.models.RecipeIngredient
import my.app.cooking_app.models.RecipeIngredientForDB
import my.app.cooking_app.utils.FBRef
import my.app.cooking_app.viewmodels.CreateRecipeViewModel
import my.app.cooking_app.viewmodels.IngredientFragmentViewModel
import my.app.cooking_app.views.WebViewActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyDialogFragment(
    private val position: Int,
    private var isIngredientFragment: Boolean = false
) : DialogFragment() {
    private var _binding: FragmentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: CreateRecipeViewModel
    private lateinit var viewModelIngredient: IngredientFragmentViewModel
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var getIngredientBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        _binding = FragmentDialogBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            viewModel = ViewModelProvider(requireActivity())[CreateRecipeViewModel::class.java]
            viewModelIngredient =
                ViewModelProvider(requireActivity())[IngredientFragmentViewModel::class.java]
            loadingProgressBar = view.findViewById(R.id.loadingProgressBar_dialog)
            getIngredientBtn = view.findViewById<Button>(R.id.dialog_button_get_ingredient)
            binding.vm = viewModelIngredient
            binding.lifecycleOwner = this

            binding.dialogIngredientChange.setOnClickListener {
                viewModelIngredient.setIsCountStateO()
            }
            if (isIngredientFragment) {
                binding.amountConstraint.visibility = View.GONE
            }
            if (position >= 0) {
                if (isIngredientFragment) {
                    val item = viewModelIngredient.ingredients.value!![position]
                    if (item.amountOfPurchase.contains("개")) {
                        viewModelIngredient.setIsCountStateTrue()
                        binding.dialogIngredientCalorieGram.setText("1")
                    } else {
                        viewModelIngredient.setIsCountStateFalse()
                        binding.dialogIngredientCalorieGram.setText("100")
                    }
                    binding.dialogIngredientAmount.setText("0")
                    binding.dialogIngredientName.setText(item.name)
                    binding.dialogIngredientPurchaseAmount.setText(item.cost)
                    binding.dialogIngredientPurchase.setText(item.amountOfPurchase.replace("개", ""))
                    binding.dialogIngredientCalorie.setText(item.calorie)


                } else {
                    val item = viewModel.liveRecipeListModel.value!!.ingredients[position]
                    if (item.amountOfPurchase.contains("개")) {
                        viewModelIngredient.setIsCountStateTrue()
                        binding.dialogIngredientCalorieGram.setText("1")
                    } else {
                        viewModelIngredient.setIsCountStateFalse()
                        binding.dialogIngredientCalorieGram.setText("100")
                    }
                    binding.dialogIngredientName.setText(item.name)
                    binding.dialogIngredientAmount.setText(item.amount.replace("개", ""))
                    binding.dialogIngredientPurchaseAmount.setText(item.cost.replace("개", ""))
                    binding.dialogIngredientPurchase.setText(item.amountOfPurchase.replace("개", ""))
                    binding.dialogIngredientCalorie.setText(item.calorie)
                }
            }

            viewModelIngredient.isCountState.observe(this, Observer {
                if (it) {
                    binding.dialogTextviewUnit.text = "단위:개수"

                } else {
                    binding.dialogTextviewUnit.text = "단위:무게(g)"

                }
            })

            view.findViewById<Button>(R.id.dialog_button_save).setOnClickListener {
                if (view.findViewById<EditText>(R.id.dialog_ingredient_name).text.toString() == "") {
                    Toast.makeText(view.context, "재료명을 입력해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    var calorie = try {
                        if (viewModelIngredient.isCountState.value!!) {
                            ((binding.dialogIngredientCalorie.text.toString()
                                .toDouble() / binding.dialogIngredientCalorieGram.text.toString()
                                .toDouble())).toInt().toString()
                        } else {
                            ((binding.dialogIngredientCalorie.text.toString()
                                .toDouble() / binding.dialogIngredientCalorieGram.text.toString()
                                .toDouble()) * 100).toInt().toString()
                        }

                    } catch (e: Exception) {
                        ""
                    }
                    val ingredient = RecipeIngredient(
                        binding.dialogIngredientName.text.toString(),
                        if (viewModelIngredient.isCountState.value!!) binding.dialogIngredientAmount.text.toString() + "개".ifEmpty { "0개" } else binding.dialogIngredientAmount.text.toString()
                            .ifEmpty { "0" },
                        binding.dialogIngredientPurchaseAmount.text.toString().ifEmpty { "0" },
                        if (viewModelIngredient.isCountState.value!!) binding.dialogIngredientPurchase.text.toString() + "개".ifEmpty { "0개" } else binding.dialogIngredientPurchase.text.toString()
                            .ifEmpty { "0" },
                        calorie,
                    )

                    if (position >= 0) {
                        if (isIngredientFragment) {
                            viewModelIngredient.edit(position, ingredient)
                        } else {
                            viewModel.editIngredient(ingredient, position)
                        }
                    } else {
                        if (isIngredientFragment) {
                            viewModelIngredient.add(ingredient)
                        } else {
                            viewModel.addIngredient(
                                ingredient
                            )
                        }
                    }
                    Toast.makeText(view.context, "저장되었습니다", Toast.LENGTH_SHORT).show()
                    dismiss()
                }
            }

            val deleteBtn = view.findViewById<Button>(R.id.dialog_button_delete)
            if (position < 0) {
                deleteBtn.visibility = View.GONE
                if (!isIngredientFragment) {
                    getIngredientBtn.visibility = View.VISIBLE
                }
            }
            getIngredientBtn.setOnClickListener {
                showListPopup()
            }
            deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
                builder.setTitle("재료 삭제")
                    .setMessage("삭제하시겠습니까?")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                            if (isIngredientFragment) {
                                viewModelIngredient.delete(position)
                            } else {
                                viewModel.deleteRecipe(position)
                            }
                            Toast.makeText(view.context, "삭제되었습니다", Toast.LENGTH_SHORT).show()

                            dismiss()
                        })
                    .setNegativeButton("취소",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                val alertDialog = builder.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                alertDialog.show()

            }
            view.findViewById<ImageView>(R.id.dialog_fragment_webview).setOnClickListener {
                val intent = Intent(requireContext(), WebViewActivity::class.java)
                intent.putExtra("name", binding.dialogIngredientName.text.toString())
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.d("MyDialogFragment", e.toString())
        }
        binding.ivInfo.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
                .setMessage(
          "1큰술(1T, 1Ts)= 1숟가락\t15ml = 3t(계량스푼이 없는 경우 밥숟가락으로 볼록하게 가득 담으면 1큰술)\n" +
                  "\n1작은술(1t, 1ts)\t5ml(티스푼으로는 2스푼이 1작은술)\n" +
                  "\n1컵(1Cup, 1C)\t200ml = 16T(한국,중국,일본)(미국 및 서양의 경우 1C가 240~250ml이므로 계량컵 구매 사용시 주의)\n" +
                  "\n1종이컵\t180ml\n" +
                  "\n1oz\t28.3g\n" +
                  "\n1파운드(lb)\t약 0.453 킬로그램(kg)\n" +
                  "\n1갤런(gallon)\t약 3.78 리터(ℓ)\n" +
                  "\n1꼬집\t약 2g 정도이며 '약간'이라고 표현하기도 함\n" +
                  "\n1줌\t한손 가득 넘치게 쥐어진 정도(예시 : 멸치 1줌 = 국멸치인 경우 12~15마리, 나물 1줌은 50g)\n" +
                  "크게 1줌 = 2줌\t1줌의 두배\n" +
                  "\n1주먹\t여자 어른의 주먹크기, 고기로는 100g\n" +
                  "\n1토막\t2~3cm두께 정도의 분량\n" +
                  "\n마늘 1톨\t깐 마늘 한쪽\n" +
                  "\n생강 1쪽\t마늘 1톨의 크기와 비슷\n" +
                  "\n생강 1톨\t아기 손바닥만한 크기의 통생강 1개\n" +
                  "\n고기 1근\t600g\n" +
                  "\n채소 1근\t400g\n" +
                  "\n채소 1봉지\t200g 정도"
                )
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
            val alertDialog = builder.create()
            alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
            alertDialog.show()
        }

    }

    private fun showListPopup() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                loadingProgressBar.visibility = View.VISIBLE
                getIngredientBtn.visibility = View.GONE
            }
            val list = mutableListOf<RecipeIngredientForDB>()
            val stringList = mutableListOf<String>()

            FBRef.myIngredients.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (postSnapshot in dataSnapshot.children) {
                        val value = postSnapshot.getValue(RecipeIngredientForDB::class.java)
                        list.add(value!!)
                        stringList.add(value.name)
                    }
                    list.reverse()
                    stringList.reverse()

                    // UI 업데이트를 Main 스레드에서 처리
                    view?.post {

                        val adapter =
                            ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                stringList
                            )

                        val listView = ListView(requireContext())
                        listView.adapter = adapter

                        val builder = AlertDialog.Builder(requireContext(), R.style.RoundedDialog)
                        builder.setTitle("재료를 선택하세요")
                            .setView(listView)
                            .setNegativeButton("취소") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val alertDialog = builder.create()
                        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alertDialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
                        alertDialog.show()
                        loadingProgressBar.visibility = View.GONE
                        getIngredientBtn.visibility = View.VISIBLE

                        listView.setOnItemClickListener { _, _, position, _ ->
                            val selectedItem = list[position]
                            if (selectedItem.amountOfPurchase.contains("개")) {
                                viewModelIngredient.setIsCountStateTrue()
                                binding.dialogIngredientCalorieGram.setText("1")
                            } else {
                                viewModelIngredient.setIsCountStateFalse()
                                binding.dialogIngredientCalorieGram.setText("100")
                            }
                            binding.dialogIngredientName.setText(selectedItem.name)
                            binding.dialogIngredientPurchase.setText(
                                selectedItem.amountOfPurchase.replace(
                                    "개",
                                    ""
                                )
                            )
                            binding.dialogIngredientPurchaseAmount.setText(selectedItem.cost)
                            binding.dialogIngredientCalorie.setText(selectedItem.calorie)

                            alertDialog.dismiss()
                            binding.dialogIngredientAmount.requestFocus()
                        }

                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // 데이터 읽기가 취소될 때 처리
                    println("Read failed: " + databaseError.toException())
                }
            })
        }
    }

}