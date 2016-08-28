package equipe.projetoes.utilis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.models.Livro;

/**
 * Created by Victor Batista on 29/04/2016.
 */
public class LivroDAO {

    private DBHelper dbHelper;

    public LivroDAO(Context context) {

        dbHelper = new DBHelper(context);
    }

    /**
     * Adiciona objeto no banco de dados.
     */
    public final void adiciona(Livro livro) {
        // Encapsula no objeto do tipo ContentValues os valores a serem persistidos no banco de dados
        ContentValues values = new ContentValues();
        values.put("name", livro.getNome());
        values.put("autor", livro.getAutor());
        values.put("editora", livro.getEditora());
        values.put("isbn", livro.getISBN());
        values.put("pg", livro.getPg());
        values.put("read_pg", livro.getReadPg());
        values.put("fav", livro.isFav());
        values.put("trade", livro.isTradable());
        values.put("imgpath", livro.getImgFilePath());

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        // Insere o registro no banco de dados
        long identificador = dbase.insert("livros", null, values);
        livro.setId(identificador);
        // Encerra a conexão com o banco de dados
        dbase.close();
    }

    public final Livro getLivroByName(String nome) {
        // Cria um List para guardar os objetos consultados no banco de dados
        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
        // Executa a consulta no banco de dados
        Cursor cursor = dbase.query("livros", null, "name = " + "'" + nome + "'", null, null,
                null, null);
        /**
         * Percorre o Cursor, injetando os dados consultados em um objeto
         * do tipo ObjetoEmprestado e adicionando-os na List
         */
        Livro livro = new Livro();
        try {
            while (cursor.moveToNext()) {

                livro.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                livro.setNome(cursor.getString(cursor.getColumnIndex("name")));
                livro.setAutor(cursor.getString(cursor.getColumnIndex("autor")));
                livro.setEditora(cursor.getString(cursor.getColumnIndex("editora")));
                livro.setISBN(cursor.getString(cursor.getColumnIndex("isbn")));
                livro.setPg(cursor.getInt(cursor.getColumnIndex("pg")));
                livro.setReadPg(cursor.getInt(cursor.getColumnIndex("read_pg")));
                livro.setFav(cursor.getInt(cursor.getColumnIndex("fav")) > 0);
                livro.setTradable(cursor.getInt(cursor.getColumnIndex("trade")) > 0);
                livro.setImgFilePath(cursor.getString(cursor.getColumnIndex("imgpath")));

                if (livro.getNome().equals(nome))
                    break;
            }
        } finally {
            // Encerra o Cursor
            cursor.close();
        }
        // Encerra a conexão com o banco de dados
        dbase.close();
        return livro;
    }

    /**
     * Lista todos os registros da tabela
     */
    public final List<Livro> listaTodos() {
        // Cria um List para guardar os objetos consultados no banco de dados
        List<Livro> Livros = new ArrayList<Livro>();
        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
        // Executa a consulta no banco de dados
        Cursor cursor = dbase.query("Livros", null, null, null, null,
                null, null);
        /**
         * Percorre o Cursor, injetando os dados consultados em um objeto
         * do tipo ObjetoEmprestado e adicionando-os na List
         */
        try {
            while (cursor.moveToNext()) {
                Livro Livro = new Livro();
                Livro.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                Livro.setNome(cursor.getString(cursor.getColumnIndex("name")));
                Livro.setAutor(cursor.getString(cursor.getColumnIndex("autor")));
                Livro.setEditora(cursor.getString(cursor.getColumnIndex("editora")));
                Livro.setISBN(cursor.getString(cursor.getColumnIndex("isbn")));
                Livro.setPg(cursor.getInt(cursor.getColumnIndex("pg")));
                Livro.setReadPg(cursor.getInt(cursor.getColumnIndex("read_pg")));
                Livro.setFav(cursor.getInt(cursor.getColumnIndex("fav")) > 0);
                Livro.setTradable(cursor.getInt(cursor.getColumnIndex("trade")) > 0);
                Livro.setImgFilePath(cursor.getString(cursor.getColumnIndex("imgpath")));
                Livros.add(Livro);
            }
        } finally {
            // Encerra o Cursor
            cursor.close();
        }
        // Encerra a conexão com o banco de dados
        dbase.close();
        // Retorna uma lista com os objetos consultados
        return Livros;
    }

    public final List<Livro> listaTradables() {
        // Cria um List para guardar os objetos consultados no banco de dados
        List<Livro> Livros = new ArrayList<Livro>();
        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
        // Executa a consulta no banco de dados
        Cursor cursor = dbase.query("livros", null, "trade = 1", null, null,
                null, null);
        /**
         * Percorre o Cursor, injetando os dados consultados em um objeto
         * do tipo ObjetoEmprestado e adicionando-os na List
         */
        try {
            while (cursor.moveToNext()) {
                Livro Livro = new Livro();
                Livro.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                Livro.setNome(cursor.getString(cursor.getColumnIndex("name")));
                Livro.setAutor(cursor.getString(cursor.getColumnIndex("autor")));
                Livro.setEditora(cursor.getString(cursor.getColumnIndex("editora")));
                Livro.setISBN(cursor.getString(cursor.getColumnIndex("isbn")));
                Livro.setPg(cursor.getInt(cursor.getColumnIndex("pg")));
                Livro.setReadPg(cursor.getInt(cursor.getColumnIndex("read_pg")));
                Livro.setFav(cursor.getInt(cursor.getColumnIndex("fav")) > 0);
                Livro.setTradable(cursor.getInt(cursor.getColumnIndex("trade")) > 0);
                Livro.setImgFilePath(cursor.getString(cursor.getColumnIndex("imgpath")));
                Livros.add(Livro);
            }
        } finally {
            // Encerra o Cursor
            cursor.close();
        }
        // Encerra a conexão com o banco de dados
        dbase.close();
        // Retorna uma lista com os objetos consultados
        return Livros;
    }

    /**
     * Método responsável por atualizar os dados de um Livro que ja está armazenado
     * no banco de dados.
     */
    public final void atualizaDadosDoLivro(Livro Livro) {
        String tableName = "livros";
        String where;
        if (Livro.getId() == 0)
            where = "_id = " + getLivroByName(Livro.getNome()).getId();
        else
            where = "_id = " + Livro.getId();

        ContentValues values = new ContentValues();
        values.put("name", Livro.getNome());
        values.put("autor", Livro.getAutor());
        values.put("editora", Livro.getEditora());
        values.put("isbn", Livro.getISBN());
        values.put("pg", Livro.getPg());
        values.put("read_pg", Livro.getReadPg());
        values.put("fav", Livro.isFav());
        values.put("trade", Livro.isTradable());
        values.put("imgpath", Livro.getImgFilePath());

        System.out.println("atualiza: " + Livro.getImgFilePath());
        System.out.println("atualiza: " + where);
        System.out.println("atualiza: " + Livro.getId());
        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        dbase.update(tableName, values, where, null);


        dbase.close();
    }

}
