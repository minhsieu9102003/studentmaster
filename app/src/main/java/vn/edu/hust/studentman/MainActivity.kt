// app/src/main/java/vn/edu/hust/studentman/MainActivity.kt
package vn.edu.hust.studentman

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

  private lateinit var studentAdapter: StudentAdapter
  private val students = mutableListOf<StudentModel>()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    // Set the content view to the activity_main layout
    setContentView(R.layout.activity_main)

    // Initialize the students list with initial data
    students.addAll(
      listOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
      )
    )

    // Initialize the adapter with edit and delete click listeners
    studentAdapter = StudentAdapter(
      students = students,
      onEditClick = { student, position ->
        showEditStudentDialog(student, position)
      },
      onDeleteClick = { student, position ->
        showDeleteConfirmationDialog(student, position)
      }
    )

    // Set up the RecyclerView
    findViewById<RecyclerView>(R.id.recycler_view_students).apply {
      adapter = studentAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    // Set up the "Add new" button
    findViewById<Button>(R.id.btn_add_new).setOnClickListener {
      showAddStudentDialog()
    }
  }

  private fun showAddStudentDialog() {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val studentNameEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val studentIdEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    AlertDialog.Builder(this)
      .setTitle("Add New Student")
      .setView(dialogView)
      .setPositiveButton("Add") { _, _ ->
        val name = studentNameEditText.text.toString()
        val id = studentIdEditText.text.toString()
        if (name.isNotEmpty() && id.isNotEmpty()) {
          val newStudent = StudentModel(name, id)
          students.add(newStudent)
          studentAdapter.notifyItemInserted(students.size - 1)
        } else {
          Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun showEditStudentDialog(student: StudentModel, position: Int) {
    val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
    val studentNameEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
    val studentIdEditText = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

    // Set current student data
    studentNameEditText.setText(student.studentName)
    studentIdEditText.setText(student.studentId)

    AlertDialog.Builder(this)
      .setTitle("Edit Student")
      .setView(dialogView)
      .setPositiveButton("Update") { _, _ ->
        val updatedName = studentNameEditText.text.toString()
        val updatedId = studentIdEditText.text.toString()
        if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
          students[position] = StudentModel(updatedName, updatedId)
          studentAdapter.notifyItemChanged(position)
        } else {
          Toast.makeText(this, "Please enter all details", Toast.LENGTH_SHORT).show()
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun showDeleteConfirmationDialog(student: StudentModel, position: Int) {
    AlertDialog.Builder(this)
      .setTitle("Delete Student")
      .setMessage("Are you sure you want to delete ${student.studentName}?")
      .setPositiveButton("Delete") { _, _ ->
        deleteStudent(student, position)
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  private fun deleteStudent(student: StudentModel, position: Int) {
    students.removeAt(position)
    studentAdapter.notifyItemRemoved(position)
    showUndoSnackbar(student, position)
  }

  private fun showUndoSnackbar(deletedStudent: StudentModel, deletedPosition: Int) {
    val view = findViewById<View>(R.id.main)
    Snackbar.make(view, "${deletedStudent.studentName} has been deleted", Snackbar.LENGTH_LONG)
      .setAction("Undo") {
        students.add(deletedPosition, deletedStudent)
        studentAdapter.notifyItemInserted(deletedPosition)
      }
      .show()
  }
}
