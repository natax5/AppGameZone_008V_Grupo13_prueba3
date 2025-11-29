package com.example.gamezone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.db.AppDatabase
import com.example.gamezone.db.User
import kotlinx.coroutines.launch

class UserManagementActivity : AppCompatActivity() {
    private lateinit var rvUsers: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_management)

        rvUsers = findViewById(R.id.rvUsers)
        rvUsers.layoutManager = LinearLayoutManager(this)

        findViewById<Button>(R.id.btnBack).setOnClickListener {
            finish()
        }

        loadUsers()
    }

    private fun loadUsers() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val users = db.userDao().getAllUsers()
            rvUsers.adapter = UsersAdapter(users.toMutableList())
        }
    }

    // Adaptador Interno con botón de eliminar
    inner class UsersAdapter(private val users: MutableList<User>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
        
        inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val name: TextView = view.findViewById(R.id.tvUserName)
            val email: TextView = view.findViewById(R.id.tvUserEmail)
            val role: TextView = view.findViewById(R.id.tvUserRole)
            val btnDelete: Button = view.findViewById(R.id.btnDeleteUser)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_user_admin, parent, false)
            return UserViewHolder(view)
        }

        override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
            val user = users[position]
            val role = if (user.email.endsWith("@admin.cl")) "ADMIN" else "CLIENTE"
            
            holder.name.text = user.fullName
            holder.email.text = user.email
            holder.role.text = "Rol: $role"

            holder.btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    val db = AppDatabase.getDatabase(applicationContext)
                    db.userDao().deleteUser(user.id)
                    
                    Toast.makeText(this@UserManagementActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                    users.removeAt(holder.adapterPosition)
                    notifyItemRemoved(holder.adapterPosition)
                    notifyItemRangeChanged(holder.adapterPosition, users.size)
                }
            }
        }

        override fun getItemCount() = users.size
    }
}