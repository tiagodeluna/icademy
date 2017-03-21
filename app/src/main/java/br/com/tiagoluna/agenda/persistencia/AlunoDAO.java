package br.com.tiagoluna.agenda.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import br.com.tiagoluna.agenda.dominio.Aluno;

/**
 * Created by tiago on 09/02/2017.
 */

public class AlunoDAO extends SQLiteOpenHelper {

    private static final String DB_NAME = "Agenda";
    private static final int DB_VERSION = 2;
    public static final String TABLE = "Alunos";

    public AlunoDAO(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ TABLE +" (id INTEGER PRIMARY KEY," +
                "nome TEXT NOT NULL, " +
                "endereco TEXT," +
                "telefone TEXT," +
                "site TEXT," +
                "nota REAL," +
                "caminhoFoto TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        //Update table
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE "+ TABLE +" ADD COLUMN caminhoFoto TEXT;";
                db.execSQL(sql);
            case 2:
                //Current version
                break;
        }
    }

    public void inserir(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = getDadosAluno(aluno);

        db.insert("Alunos", null, dados);
    }

    public List<Aluno> buscarAlunos() {
        String sql = "SELECT * FROM "+ TABLE +" ORDER BY nome, nota DESC, id;";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(sql, null);

        List<Aluno> alunos = new ArrayList<>();

        while(c.moveToNext()) {
            Aluno a = new Aluno();

            a.setId(c.getLong(c.getColumnIndex("id")));
            a.setNome(c.getString(c.getColumnIndex("nome")));
            a.setEndereco(c.getString(c.getColumnIndex("endereco")));
            a.setTelefone(c.getString(c.getColumnIndex("telefone")));
            a.setSite(c.getString(c.getColumnIndex("site")));
            a.setNota(c.getDouble(c.getColumnIndex("nota")));
            a.setCaminhoFoto(c.getString(c.getColumnIndex("caminhoFoto")));

            alunos.add(a);
        }

        c.close();

        return alunos;
    }

    public void excluir(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        String[] params = {aluno.getId().toString()};

        db.delete(TABLE, "id = ?", params);
    }

    public void alterar(Aluno aluno) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues dados = new ContentValues();
        String[] params = {aluno.getId().toString()};

        db.update(TABLE, this.getDadosAluno(aluno), "id = ?", params);
    }

    @NonNull
    private ContentValues getDadosAluno(Aluno aluno) {
        ContentValues dados = new ContentValues();

        dados.put("nome", aluno.getNome());
        dados.put("endereco", aluno.getEndereco());
        dados.put("telefone", aluno.getTelefone());
        dados.put("site", aluno.getSite());
        dados.put("nota", aluno.getNota());
        dados.put("caminhoFoto", aluno.getCaminhoFoto());

        return dados;
    }

    public boolean isAluno(String telefone) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE + " WHERE telefone = ?", new String[]{telefone});

        int count = result.getCount();
        //Close the cursor
        result.close();

        return count > 0;
    }
}
