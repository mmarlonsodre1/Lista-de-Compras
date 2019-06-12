package br.edu.infnet.listadecompras;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText mEdtProduto;
    private ListView mLvwProdutos;
    private List<String> mLista = new ArrayList<>();
    private Button mBtnSalvar;
    private static final String TAG = "LISTA_COMPRAS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEdtProduto = findViewById(R.id.edt_produto);
        mLvwProdutos = findViewById(R.id.lvw_produtos);
        Button btnAdicionar = findViewById(R.id.btn_plus);
        btnAdicionar.setOnClickListener(adicionar);

        Button mBtnSalvar = findViewById(R.id.btnSalvar);
        mBtnSalvar.setOnClickListener(salvar);

        carregarLista();
    }

    View.OnClickListener adicionar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //pegar string do edittext e cortar o espaço sobrando
            String produto = mEdtProduto.getText().toString().trim();
            //caso produto não esteja vazio e lista não contenha o produto
            if (!produto.isEmpty() && !mLista.contains(produto)) {
                mLista.add(produto);


                String[] lista = mLista.toArray(new String[mLista.size()]);
                mLvwProdutos.setAdapter(new ArrayAdapter<String>(getApplicationContext(),
                        R.layout.list_view_item, R.id.txtItem, lista));

                mEdtProduto.setText("");
                mEdtProduto.requestFocus();
            }
        }
    };

    AdapterView.OnItemLongClickListener remover = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mLista.remove(position);
            String[] lista = mLista.toArray(new String[mLista.size()]);
            mLvwProdutos.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_item,
                    R.id.txtItem, lista));
            return true;

        }
    };

    View.OnClickListener salvar = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StringBuilder texto = new StringBuilder("");

            for (String valor : mLista) {
                texto.append(valor).append(";");
            }
            try {
                FileOutputStream fos = openFileOutput("lista_de_compras", Context.MODE_PRIVATE);
                fos.write(texto.toString().getBytes());
            } catch (FileNotFoundException exception) {
                Log.e(TAG, "Não foi possível carregar o arquivo!");
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            } catch (IOException exception) {
                Log.e(TAG, "ERROR");
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                exception.printStackTrace();
            }

            Toast.makeText(getApplicationContext(), "Arquivo Salvo", Toast.LENGTH_SHORT).show();
        }
    };

    private void carregarLista(){
        StringBuilder itens = new StringBuilder("");
        try {
            FileInputStream fis = openFileInput("lista_de_compras");
            byte[] buffer = new byte[(int) fis.getChannel().size()];
            fis.read(buffer);
            for (byte b : buffer)
                itens.append((char)b);
        } catch (FileNotFoundException exception) {
            Log.e(TAG,"Não foi possível encontrar o arquivo!", exception);
            Toast.makeText(getApplicationContext(), "Não foi possível encontrar o arquivo!", Toast.LENGTH_SHORT).show();
            exception.printStackTrace();
        } catch (IOException exception){
            Log.e(TAG, "Não foi possível ler o arquivo!", exception);
            Toast.makeText(getApplicationContext(), "Não foi possível encontrar o arquivo", Toast.LENGTH_SHORT).show();
        }

        String[] lista = itens.toString().split(";");
        mLvwProdutos.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view_item, R.id.txtItem, lista));
        mLista.addAll(Arrays.asList(lista));

    }

}

