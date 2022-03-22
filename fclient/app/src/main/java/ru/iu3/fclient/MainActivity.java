package ru.iu3.fclient;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.IOUtils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ru.iu3.fclient.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity implements TransactionEvents {

    // Used to load the 'fclient' library on application startup.
    static {
        System.loadLibrary("fclient");
        System.loadLibrary("mbedcrypto");
    }

    private ActivityMainBinding binding;
    ActivityResultLauncher activityResultLauncher;

    public static byte[] stringToHex(String s)
    {
        byte[] hex;
        try
        {
            hex = Hex.decodeHex(s.toCharArray());
        }
        catch (DecoderException ex)
        {
            hex = null;
        }
        return hex;
    }
    /*Обработчик transactionResult , будет вызываться из неуправляемого кода и передавать в
    виртуальную машину результат операции. Вот подходящая реализация этого обработчика в классе
    MainActivity.*/

    @Override
    public void transactionResult(boolean result) {
        runOnUiThread(()-> {
            Toast.makeText(MainActivity.this, result ? "ok" : "failed", Toast.LENGTH_SHORT);
        });
    }

    private String pin;

/*    Функция enterPin запускает форму для ввода ПИН-кода, передает ей счетчик попыток ввода и сумму,
    и ожидает завершения ввода. Обработчик onActivityResult копирует значение ПИН-кода в поле pin,
    если он введен. Это значение возвращается функцией enterPin обратно в код C++.*/
    @Override
    public String enterPin(int ptc, String amount) {
        pin = new String();
        Intent it = new Intent(MainActivity.this, PinpadActivity.class);
        it.putExtra("ptc", ptc);
        it.putExtra("amount", amount);
        synchronized (MainActivity.this) {
            activityResultLauncher.launch(it);
            try {
                MainActivity.this.wait();
            } catch (Exception ex) {
                //todo: log error
            }
        }
        return pin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int res = initRng();
        byte[] d_key = randomBytes(16);

        Random random = new Random();

        byte[] mas = new byte[20];
        for (int i = 0; i < mas.length; ++i) {
            mas[i] = (byte) ((byte) random.nextInt() % 255);
        }

        // Пример шифрованя данных (в отладчике)
        byte[] encrypt_mas = encrypt(d_key, mas);

        // Пример дешифрования данных (в отладчике)
        byte[] decrypt_mas = decrypt(d_key, encrypt_mas);
        // Example of a call to a native method
        //TextView tv = findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        activityResultLauncher
                = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                        // обработка результата
                        //String pin = data.getStringExtra("pin");
                        //Toast.makeText(MainActivity.this, pin, Toast.LENGTH_SHORT);
                            pin = data.getStringExtra("pin");
                            synchronized (MainActivity.this) {
                               MainActivity.this.notifyAll();
                            }
                        }
                    }
                });

    }
    protected String getPageTitle(String html)
    {
        Pattern pattern = Pattern.compile("<title>(.+?)</title>", Pattern.DOTALL); // Ленивый режим квантификатора
        Matcher matcher = pattern.matcher(html);
        String p;
        if (matcher.find())
            p = matcher.group(1);
        else
            p = "Not found";
        return p;
    }
     /*Этот метод создает и отправляет запрос web серверу. Получает ответ, извлекает из него тег <title> и отображает его во
    всплывающем сообщении на экране*/

    /*HTTP запрос запускается в отдельном потоке, чтобы не блокировать пользовательcкий интерфейс.
    Процедура потока задается с помощью лямбда-выражения ()-> в конструкторе класса Thread.*/
    protected void testHttpClient()
    {
        new Thread(() -> {
            try {
                HttpURLConnection uc = (HttpURLConnection) (new URL("http://10.0.2.2:8081/api/v1/title").openConnection());
                //HttpURLConnection uc = (HttpURLConnection) (new URL("http://109.252.162.164:8081/api/v1/title").openConnection());
                InputStream inputStream = uc.getInputStream();//возвращает результат GET (по умолчанию) запроса в переменой типа InputStream
                String html = IOUtils.toString(inputStream);
                String title = getPageTitle(html); //нужна для извлечения содержимого тега <title> из HTML-страницы

                /*Мы не можем отобразить результат в фоновом потоке (вспомните курс Технология Программирования) поэтому используем
                метод runOnUiThread и, кстати, снова лямбда-выражение, для того, чтобы отобразить результат на экране в потоке
                пользовательского интерфейса.*/

                runOnUiThread(() ->
                {
                    Toast.makeText(this, title, Toast.LENGTH_LONG).show();
                });
            } catch (Exception ex) {
                Log.e("fapptag", "Http client fails", ex);
            }
        }).start();
    }


    public void onButtonClick(View v)
    {
        //Intent it = new Intent(this, PinpadActivity.class);
        //при StartActivity нет вывода сообщения. Используем  activityResultLauncher
        //activityResultLauncher.launch(it);

        //startActivity(it);
        //Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        //byte[] key =
         //       stringToHex("0123456789ABCDEF0123456789ABCDE0");
        //byte[] enc = encrypt(key,
        //        stringToHex("000000000000000102"));
        //byte[] dec = decrypt(key, enc);
        //String s = new String(Hex.encodeHex(dec)).toUpperCase();
        //Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

        new Thread(()-> {
            try {
                byte[] trd = stringToHex("9F0206000000000100");
                //transaction(trd);
                testHttpClient();

            } catch (Exception ex) {
            // todo: log error
            }
        }).start();
    }

    /**
     * A native method that is implemented by the 'fclient' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
    public native boolean transaction(byte[] trd);
    public static native int initRng();
    public static native byte[] randomBytes(int no);
    public static native byte[] encrypt(byte[] key, byte[] data);
    public static native byte[] decrypt(byte[] key, byte[] data);
}