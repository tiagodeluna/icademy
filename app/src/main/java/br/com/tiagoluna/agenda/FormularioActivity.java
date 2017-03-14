package br.com.tiagoluna.agenda;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import br.com.tiagoluna.agenda.dominio.Aluno;
import br.com.tiagoluna.agenda.persistencia.AlunoDAO;

public class FormularioActivity extends AppCompatActivity {

    public static final int CODIGO_CAMERA = 1;

    private FormularioHelper helper;
    private AlunoDAO dao;
    private String caminhoFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        this.helper = new FormularioHelper(this);
        this.dao = new AlunoDAO(this);

        //Carrega dados do aluno para edição, se tiver
        carregarAluno();

        //Implementa ação do botão de Foto
        Button botao_foto = (Button) findViewById(R.id.formulario_botao_foto);
        botao_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                //Monta caminho para o arquivo e instancia arquivo
                caminhoFoto = getExternalFilesDir(null)+"/"+System.currentTimeMillis()+".jpg";
                File arquivoFoto = new File(caminhoFoto);

                //A partir da versão 24 da SDK, deve-se usar a classe FileProvider para acessar
                // os diretórios do dispositivo e gravar arquivos. Para tal, é necessário criar
                // o arquivo provider_paths.xml e adicionar a tag <provider> no AndroidManifest.xml,
                // apontando para esse arquivo.
                Context context = FormularioActivity.this.getApplicationContext();
                Uri photoURI = FileProvider.getUriForFile(context,
                        context.getApplicationContext().getPackageName() + ".provider", arquivoFoto);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intentCamera, CODIGO_CAMERA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Verifica se a foto foi realmente tirada ou se o usuário cancelou
        if (resultCode == Activity.RESULT_OK) {
            //Abrir foto tirada
            if (requestCode == CODIGO_CAMERA) {
                helper.carregarImagem(caminhoFoto);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_formulario, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_formulario_ok:
                Aluno aluno = this.helper.obterAluno();

                if (this.helper.getAluno().getId() == null) {
                    inserirAluno(aluno);
                    Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " salvo com sucesso!", Toast.LENGTH_SHORT).show();
                }
                else {
                    alterarAluno(aluno);
                    Toast.makeText(FormularioActivity.this, "Aluno " + aluno.getNome() + " alterado com sucesso!", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void alterarAluno(Aluno aluno) {
        this.dao.alterar(aluno);
        this.dao.close();
    }

    private void inserirAluno(Aluno aluno) {
        this.dao.inserir(aluno);
        this.dao.close();
    }

    private void carregarAluno() {
        //Recupera intent usada pra abrir essa tela
        Intent intent = getIntent();
        //Obtém aluno pendurado na intent
        Aluno aluno = (Aluno) intent.getSerializableExtra("aluno");
        //Se um aluno tiver sido informado, carrega-o na tela para edição
        if (aluno != null) {
            this.helper.carregarAluno(aluno);
        }
    }
}
