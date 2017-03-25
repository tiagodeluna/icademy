package br.com.tiagoluna.agenda.converter;

import org.json.JSONException;
import org.json.JSONStringer;

import java.util.List;

import br.com.tiagoluna.agenda.dominio.Aluno;

/**
 * Created by tiago on 24/03/2017.
 */

public class AlunoConverter {

    public String converterParaJson(List<Aluno> lista) {
        JSONStringer js = new JSONStringer();

        try {
            //Opens list of objects
            js.object().key("list").array().object().key("student").array();
            //Fills file with students
            for (Aluno a : lista) {
                js.object();
                js.key("id").value(a.getId());
                js.key("nome").value(a.getNome());
                js.key("telefone").value(a.getTelefone());
                js.key("nota").value(a.getNota());
                js.endObject();
            }
            //Ends list of objects
            js.endArray().endObject().endArray().endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
