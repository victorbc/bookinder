package equipe.projetoes.data;

import java.util.List;

import equipe.projetoes.exceptions.BookinderException;
import equipe.projetoes.models.Account;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.LivroUser;
import equipe.projetoes.models.Match;
import equipe.projetoes.util.Callback;

/**
 * Created by stenio on 9/15/2016.
 *
 * Interface utilizada para manipular o Banco de dados remoto atraves da REST API
 */
public interface RestDAOInterface {

    /**
     * Metodo utilizado para autenticar um usuário a REST API.
     *
     * Um Token é retornado se o usuário for autenticado com sucesso, caso contrário uma exceção
     * será lançada
     *
     * Isso precisa ser feito antes de realizar operações relacionadas a um usuário específico.
     * @param username O username do usuário que será autenticado
     * @param password O password do usuário
     * @param callback A classe com o metodo que sera executado ao final da requisicao
     * @return A conta do usuário
     */
    void authenticate(String username, String password, final Callback<Account> callback);

    /**
     * Recupera o perfil do Usuario
     * @param callback o callback que sera executado
     */
    void getUserProfile(Callback<Account> callback);

    /**
     * This method can be used without needing to be authenticated. Anyone can create an account
     *
     * There is no verification for that. The data is not encrypted to be sent.
     * Howerver, when the password is registered, it gets encrypted on the database.
     * @param acc The new user account
     * @param callback The callback function
     * @throws BookinderException In case something goes wrong
     */
    void createUser(final Account acc, final Callback<Account> callback);

    /**
     * Cria um livro no banco de dados.
     *
     * Essa abordagem pode ser considerada temporária, pois implica em o usuário ter a informação do
     * livro para então poder existir no servidor.
     *
     * Qualquer usuário registrado pode cadastrar um ilvro.
     *
     * Se o livro já existir, ele não é criado.
     *
     * Essa função deve ser usada caso não exista o livro ao executar o get livro.
     *
     * @param livro O livro a ser criado
     * @param callback O callback a ser executado
     */
    void createLivro(Livro livro, Callback<Livro> callback);

    /**
     * Pega um livro pelo isbn
     * @param isbn Isbn do livro
     * @param callback O callback
     */
    void getLivro(String isbn, Callback<Livro> callback);

    /**
     * Recupera um livro do usuario da biblioteca pelo id do LivroUser na biblioteca
     * @param id O id do LivroUser na biblioteca (Nao confundir com o id do livro)
     * @param callback O callback que sera executado
     */
    void getLivroUser(int id, Callback<LivroUser> callback);

    /**
     * Recupera todos os livros que o usuário tem na biblioteca
     *
     * Precisar estar autenticado.
     * @param callback O callback a ser executado
     */
    void getLibrary(Callback<List<LivroUser>> callback);

    /**
     * Adiciona um livro a biblioteca do usuário
     * @param livro o livro a ser adicionado
     */
    void addBookToLibrary(Livro livro, Callback<LivroUser> callback);

    /**
     * Atualiza um dos livros do usuário.
     *
     * O id do livro passado como parâmetro tem que exisitr, pois este que será modificado.
     *
     * O livro do LivroUser também tem que ser o mesmo.
     * @param livro O Livro do usuário a ser atualizado
     * @param callback O callback a ser executado
     */
    void update(LivroUser livro, Callback<LivroUser> callback);


    void getMatchList(Callback<List<Match>> callback);
    void getPristineMatchList(Callback<List<Match>> callback);
    void rejectMatch(Match match, Callback<Integer> callback);
    void acceptMatch(Match match, Callback<Integer> callback);

    void logOff();
    boolean isAuthenticated();
}
