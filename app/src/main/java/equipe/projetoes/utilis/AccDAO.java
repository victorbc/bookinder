package equipe.projetoes.utilis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import equipe.projetoes.models.Account;

/**
 * Created by Victor Batista on 29/04/2016.
 */
public class AccDAO {

    private DBAccHelper dbHelper;
    private String tableName = "accs";

    public AccDAO(Context context) {

        dbHelper = new DBAccHelper(context);
    }

    /**
     * Adiciona objeto no banco de dados.
     */
    public final void adiciona(Account acc) {
        // Encapsula no objeto do tipo ContentValues os valores a serem persistidos no banco de dados
        ContentValues values = new ContentValues();
        values.put("login", acc.getLogin());
        values.put("email", acc.getEmail());
        values.put("pass", acc.getPass());
        values.put("firsttime", acc.isFirstTime());
        if(acc.getEmail_facebook() != null){
            values.put("email_facebook", acc.getEmail_facebook());
        }
        if(acc.getEmail_google() != null){
            values.put("email_google", acc.getEmail_google());
        }

        // Instancia uma conexão com o banco de dados, em modo de gravação
        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        // Insere o registro no banco de dados
        long identificador = dbase.insert(tableName, null, values);
        acc.setId(identificador);
        // Encerra a conexão com o banco de dados
        dbase.close();
    }

    public final Account getAccountByLogin(String login) {
        // Cria um List para guardar os objetos consultados no banco de dados
        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
        // Executa a consulta no banco de dados
        Cursor cursor = dbase.query(tableName, null, "login = " + "'" + login + "'", null, null,
                null, null);
        /**
         * Percorre o Cursor, injetando os dados consultados em um objeto
         * do tipo ObjetoEmprestado e adicionando-os na List
         */
        Account acc = new Account();
        try {
            while (cursor.moveToNext()) {

                acc.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                acc.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                acc.setPass(cursor.getString(cursor.getColumnIndex("pass")));
                acc.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                acc.setFirstTime(cursor.getInt(cursor.getColumnIndex("firsttime")) > 0);
                acc.setEmail_facebook(cursor.getString(cursor.getColumnIndex("email_facebook")));
                acc.setEmail_google(cursor.getString(cursor.getColumnIndex("email_google")));

                if (acc.getLogin().equals(login))
                    break;
            }
        } finally {
            // Encerra o Cursor
            cursor.close();
        }
        // Encerra a conexão com o banco de dados
        dbase.close();
        return acc;
    }

    /**
     * Lista todos os registros da tabela
     */
    public final List<Account> listaTodos() {
        // Cria um List para guardar os objetos consultados no banco de dados
        List<Account> Accounts = new ArrayList<Account>();
        // Instancia uma nova conexão com o banco de dados em modo leitura
        SQLiteDatabase dbase = dbHelper.getReadableDatabase();
        // Executa a consulta no banco de dados
        Cursor cursor = dbase.query(tableName, null, null, null, null,
                null, null);
        /**
         * Percorre o Cursor, injetando os dados consultados em um objeto
         * do tipo ObjetoEmprestado e adicionando-os na List
         */
        try {
            while (cursor.moveToNext()) {
                Account acc = new Account();
                acc.setId(cursor.getLong(cursor.getColumnIndex("_id")));
                acc.setLogin(cursor.getString(cursor.getColumnIndex("login")));
                acc.setPass(cursor.getString(cursor.getColumnIndex("pass")));
                acc.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                acc.setFirstTime(cursor.getInt(cursor.getColumnIndex("firsttime")) > 0);
                acc.setEmail_facebook(cursor.getString(cursor.getColumnIndex("email_facebook")));
                acc.setEmail_google(cursor.getString(cursor.getColumnIndex("email_google")));

                Accounts.add(acc);
            }
        } finally {
            // Encerra o Cursor
            cursor.close();
        }
        // Encerra a conexão com o banco de dados
        dbase.close();
        // Retorna uma lista com os objetos consultados
        return Accounts;
    }


    /**
     * Método responsável por atualizar os dados de um Account que ja está armazenado
     * no banco de dados.
     */
    public final void atualizaDadosDoAccount(Account acc) {

        String where;
        if (acc.getId() == 0)
            where = "_id = " + getAccountByLogin(acc.getLogin()).getId();
        else
            where = "_id = " + acc.getId();

        ContentValues values = new ContentValues();
        values.put("login", acc.getLogin());
        values.put("email", acc.getEmail());
        values.put("pass", acc.getPass());
        values.put("firsttime", acc.isFirstTime());
        if(acc.getEmail_facebook() != null){
            values.put("email_facebook", acc.getEmail_facebook());
        }
        if(acc.getEmail_google() != null){
            values.put("email_google", acc.getEmail_google());
        }

        SQLiteDatabase dbase = dbHelper.getWritableDatabase();
        dbase.update(tableName, values, where, null);


        dbase.close();
    }

}
