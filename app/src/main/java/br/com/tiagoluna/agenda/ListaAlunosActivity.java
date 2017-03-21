package br.com.tiagoluna.agenda;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import br.com.tiagoluna.agenda.adapter.AlunosAdapter;
import br.com.tiagoluna.agenda.dominio.Aluno;
import br.com.tiagoluna.agenda.persistencia.AlunoDAO;

public class ListaAlunosActivity extends AppCompatActivity {

    private final int SMS_REQUEST_CODE = 122;
    private final int CALL_PHONE_REQUEST_CODE = 123;

    private AlunoDAO dao;
    private ListView listaAlunos;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_alunos);

        if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.RECEIVE_SMS}, SMS_REQUEST_CODE);
        }

        this.dao = new AlunoDAO(this);

        //Obtém referência do ListView
        this.listaAlunos = (ListView) findViewById(R.id.lista_alunos);
        this.listaAlunos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Recupera item clicado
                Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(position);

                //Solicita abertura do Formulário passando o aluno para edição
                Intent intentToFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                intentToFormulario.putExtra("aluno", aluno);
                startActivity(intentToFormulario);
            }
        });

        Button btnNovoAluno = (Button) findViewById(R.id.lista_alunos_novo_aluno);
        btnNovoAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentToFormulario = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intentToFormulario);
            }
        });

        //Informa que listaAlunos receberá um context menu
        registerForContextMenu(this.listaAlunos);
    }

    @Override
    protected void onResume() {
        //super.onStart();
        super.onResume();
        carregarListaAlunos();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, final ContextMenu.ContextMenuInfo menuInfo) {
        //Recupera dados do aluno
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        final Aluno aluno = (Aluno) listaAlunos.getItemAtPosition(info.position);

        MenuItem itemLigar = menu.add("Ligar");
        itemLigar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //Checa permissão para fazer ligações
                if (ActivityCompat.checkSelfPermission(ListaAlunosActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ListaAlunosActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE_REQUEST_CODE);
                }
                else {
                    Intent intentLigar = new Intent(Intent.ACTION_CALL);
                    intentLigar.setData(Uri.parse("tel:" + aluno.getTelefone()));
                    startActivity(intentLigar);
                }
                return false;
            }
        });

        //Criando item de menu "Enviar SMS"
        MenuItem itemSMS = menu.add("Enviar SMS");
        Intent intentSMS = new Intent(Intent.ACTION_VIEW);
        intentSMS.setData(Uri.parse("sms:" + aluno.getTelefone()));
        itemSMS.setIntent(intentSMS);

        //Criando item de menu "Visualizar no Mapa"
        MenuItem itemMapa = menu.add("Visualizar no Mapa");
        Intent intentMapa = new Intent(Intent.ACTION_VIEW);
        intentMapa.setData(Uri.parse("geo:0,0?q=" + aluno.getEndereco()));
        itemMapa.setIntent(intentMapa);

        //Criando item do menu "Visitar site" sem usar .xml nem implementar OnMenuItemClickListener
        MenuItem itemSite = menu.add("Visitar Site");
        Intent intentSite = new Intent(Intent.ACTION_VIEW);
        //Obtém site do aluno
        String site = (aluno.getSite().startsWith("http://") || aluno.getSite().startsWith("https://")
                ? aluno.getSite() : "http://" + aluno.getSite());
        intentSite.setData(Uri.parse(site));
        itemSite.setIntent(intentSite);

        //Criando menu sem usar .xml, mas implementando OnMenuItemClickListener
        MenuItem itemExcluir = menu.add("Excluir");
        itemExcluir.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dao.excluir(aluno);
                dao.close();

                carregarListaAlunos();

                return true;
            }
        });
    }

    private void carregarListaAlunos() {
        //Obtém lista de alunos
        List<Aluno> alunos = dao.buscarAlunos();

        //Criar adapter pra passar a lista ao ListView
        AlunosAdapter adapter = new AlunosAdapter(this, alunos);
        listaAlunos.setAdapter(adapter);
    }
}
