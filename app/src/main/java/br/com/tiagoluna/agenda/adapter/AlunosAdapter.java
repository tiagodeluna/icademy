package br.com.tiagoluna.agenda.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.tiagoluna.agenda.ListaAlunosActivity;
import br.com.tiagoluna.agenda.R;
import br.com.tiagoluna.agenda.dominio.Aluno;

/**
 * Created by tiago on 12/03/2017.
 */

public class AlunosAdapter extends BaseAdapter {

    private final List<Aluno> alunos;
    private final Context context;

    public AlunosAdapter(Context context, List<Aluno> alunos) {
        this.context = context;
        this.alunos = alunos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Aluno aluno = alunos.get(position);

        LayoutInflater inflater = LayoutInflater.from(this.context);

        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.list_item, parent, false);
        }
        //Nome
        TextView campoNome = (TextView) view.findViewById(R.id.item_nome);
        campoNome.setText(aluno.getNome());
        //Telefone
        TextView campoTelefone = (TextView) view.findViewById(R.id.item_telefone);
        campoTelefone.setText(aluno.getTelefone());
        //Nota
        TextView campoNota = (TextView) view.findViewById(R.id.item_nota);
        campoNota.setText(aluno.getNota().toString());
        //Foto
        ImageView campoFoto = (ImageView) view.findViewById(R.id.item_foto);
        String caminhoFoto = aluno.getCaminhoFoto();
        if (caminhoFoto != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoFoto);
            Bitmap bitmapReduzido = Bitmap.createScaledBitmap(bitmap, 100, 100, true);
            campoFoto.setImageBitmap(bitmapReduzido);
            campoFoto.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        return view;
    }

    @Override
    public int getCount() {
        return this.alunos.size();
    }

    @Override
    public Object getItem(int position) {
        return this.alunos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.alunos.get(position).getId();
    }

}
