package com.example.filmsearch.ui.main.view

import android.Manifest
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.filmsearch.R
import com.example.filmsearch.databinding.MainActivityBinding
import kotlinx.android.synthetic.main.item_history.*
import android.content.Intent
import android.net.Uri
import android.provider.Settings


class MainActivity : AppCompatActivity() {

    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
        when{
            result -> getContact()
            !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) -> {
               // Toast.makeText(this, "No access to permission", Toast.LENGTH_LONG).show()
                AlertDialog.Builder(this )
                    .setTitle("Доступ к контактам")

                    .setMessage("Вы запретили доступ к контактам \n" +
                            "и теперь мы не можем украсть Ваши контакты.\n" +
                            "Не хотите ли открыть доступ?")

                    .setPositiveButton("Открыть настройки") { _, _ ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)

                        val uri: Uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivity(intent)
                    }
                    .setNegativeButton("МИЛИЦИЯ!") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()

            }
            else -> Toast.makeText(this, "No access to permission", Toast.LENGTH_LONG).show()
        }
    }

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.idHistory -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.getContact -> {
                permissionResult.launch(Manifest.permission.READ_CONTACTS)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {
       // contentResolver есть у контекста для того, чтобы получить доступ к контент провайдеру
        //курсор-объект, который получаем у контент провайдера.

        val cursor: Cursor? = contentResolver.query( //обращаемся по запросу к контент провайдеру
            ContactsContract.Contacts.CONTENT_URI,  // по какому урлу
            null,  //передает параметры
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"//говорим, какое поле нам нужно + сортировка по алфавиту
        ) //нам не дают все контакты сразу. дают курсор, который нацелен на первый элемент. мы его можем просить сдвинуть на второй
            //данные приходят с курсором

        val contacts = mutableListOf<String>()
        cursor?.let {
            for(i in 0..cursor.count){
                //переходим на позицию и курсор
                if (cursor.moveToPosition(i)){
                    //берем из курсор столбец с именем
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    contacts.add(name)
                }
            }
            it.close()
        }
        AlertDialog.Builder(this)
            .setItems(contacts.toTypedArray(), {d, w -> })
            .setCancelable(true)
            .show()

    }
}
