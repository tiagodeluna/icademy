package br.com.tiagoluna.agenda;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.util.List;

import br.com.tiagoluna.agenda.converter.AlunoConverter;
import br.com.tiagoluna.agenda.dominio.Aluno;
import br.com.tiagoluna.agenda.persistencia.AlunoDAO;

/**
 * Implements the secondary thread responsible to convert and send the contacts list to the
 * Web Service. The AsyncTask generics extension is customized to handle a String object.Object
 *
 * Created by tiago on 26/03/2017.
 */

public class SendContactTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private ProgressDialog progress;

    public SendContactTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(context, "Wait", "Sending contacts...", true, true);
    }

    @Override
    protected String doInBackground(Void... params) {
        AlunoDAO dao = new AlunoDAO(context);
        List<Aluno> alunos = dao.buscarAlunos();
        dao.close();

        //Creates file with all contacts to send via WebService
        AlunoConverter conversor = new AlunoConverter();
        String json = conversor.converterParaJson(alunos);

        WebClient client = new WebClient();
        return client.post(json);
    }

    @Override
    protected void onPostExecute(String response) {
        progress.dismiss();
        Toast.makeText(context, "Enviando notas..."+response, Toast.LENGTH_LONG).show();
        super.onPostExecute(response);
    }
}
