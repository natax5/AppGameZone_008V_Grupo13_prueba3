package com.example.gamezone

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamezone.api.ApiClient
import com.example.gamezone.models.UserData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        ApiClient.instance.getAllUsers().enqueue(object : Callback<List<UserData>> {
            override fun onResponse(call: Call<List<UserData>>, response: Response<List<UserData>>) {
                if (response.isSuccessful) {
                    val users = response.body() ?: emptyList()
                    rvUsers.adapter = UsersAdapter(users.toMutableList())
                } else {
                    Toast.makeText(this@UserManagementActivity, "Error al cargar usuarios", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<UserData>>, t: Throwable) {
                Toast.makeText(this@UserManagementActivity, "Error de red", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Adaptador Interno con botón de eliminar
    inner class UsersAdapter(private val users: MutableList<UserData>) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {
        
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
                // Llamar al API para borrar
                ApiClient.instance.deleteUser(user.id).enqueue(object : Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@UserManagementActivity, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                            // Remover de la lista local y notificar al adaptador
                            users.removeAt(holder.adapterPosition)
                            notifyItemRemoved(holder.adapterPosition)
                        } else {
                            Toast.makeText(this@UserManagementActivity, "Error al eliminar", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        Toast.makeText(this@UserManagementActivity, "Error de red", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        override fun getItemCount() = users.size
    }
}