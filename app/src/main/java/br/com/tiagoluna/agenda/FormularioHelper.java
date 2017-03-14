package br.com.tiagoluna.agenda;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;

import br.com.tiagoluna.agenda.dominio.Aluno;

/**
 * Created by tiago on 08/02/2017.
 */

public class FormularioHelper {

    private final EditText campoNome;
    private final EditText campoEndereco;
    private final EditText campoSite;
    private final EditText campoTelefone;
    private final RatingBar campoNota;
    private final ImageView campoFoto;

    private Aluno aluno;

    public FormularioHelper(FormularioActivity activity) {
        campoNome = (EditText) activity.findViewById(R.id.formulario_nome);
        campoEndereco = (EditText) activity.findViewById(R.id.formulario_endereco);
        campoSite = (EditText) activity.findViewById(R.id.formulario_site);
        campoTelefone = (EditText) activity.findViewById(R.id.formulario_telefone);
        campoNota = (RatingBar) activity.findViewById(R.id.formulario_nota);
        campoFoto = (ImageView) activity.findViewById(R.id.formulario_foto);
        aluno = new Aluno();
    }

    public Aluno obterAluno() {
        aluno.setNome( campoNome.getText().toString() );
        aluno.setTelefone( campoTelefone.getText().toString() );
        aluno.setSite( campoSite.getText().toString() );
        aluno.setEndereco( campoEndereco.getText().toString() );
        aluno.setNota(Double.valueOf(campoNota.getProgress()));
        aluno.setCaminhoFoto((String) campoFoto.getTag());

        return aluno;
    }

    public void carregarAluno(Aluno aluno) {
        this.aluno = aluno;
        campoNome.setText(aluno.getNome());
        campoTelefone.setText(aluno.getTelefone());
        campoSite.setText(aluno.getSite());
        campoEndereco.setText(aluno.getEndereco());
        campoNota.setProgress(aluno.getNota().intValue());
        carregarImagem(aluno.getCaminhoFoto());
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void carregarImagem(String caminhoFoto) {
        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            //Recupera foto salva
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            //Reduz a qualidade da imagem
            bitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);
            campoFoto.setImageBitmap(bitmap);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
            campoFoto.setTag(caminhoFoto);
        }
    }
}
