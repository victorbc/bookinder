package equipe.projetoes.utilis;

import java.util.List;

import equipe.projetoes.exceptions.BookinderException;
import equipe.projetoes.models.Account;
import equipe.projetoes.models.Livro;
import equipe.projetoes.models.LivroUser;

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
     * @return A conta do usuário
     * @throws BookinderException Exceção caso o usuário não seja autenticado
     */
    public Account authenticate(String username, String password) throws BookinderException;

    /**
     * This method can be used without needing to be authenticated. Anyone can create an account
     *
     * There is no verification for that. The data is not encrypted to be sent.
     * Howerver, when the password is registered, it gets encrypted on the database.
     * @param acc The new user account
     * @throws BookinderException In case something goes wrong
     */
    public Account create_user(Account acc) throws BookinderException;

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
     * @throws BookinderException Se algo der errado
     */
    public void create_book(Livro livro) throws BookinderException;

    /**
     * Returns a list of books that matchs someway **text**
     *
     * @param text The text to be matched
     * @return A list of Livros
     */
    public List<Livro> getLivrosWith(String text);

    /**
     * Pega um livro pelo isbn
     * @param isbn Isbn do livro
     * @return o livro
     */
    public Livro getLivro(String isbn);

    /**
     * Recupera todos os livros que o usuário tem na biblioteca
     *
     * Precisar estar autenticado.
     * @return Os livros na biblioteca do usuário
     */
    public List<LivroUser> getLibrary();

    /**
     * Adiciona um livro a biblioteca do usuário
     * @param livro o livro a ser adicionado
     * @return O livro para o usuário
     * @throws BookinderException Se algo der errado
     */
    public LivroUser addBookToLibrary(Livro livro) throws BookinderException;

    /**
     * Atualiza um dos livros do usuário.
     *
     * O id do livro passado como parâmetro tem que exisitr, pois este que será modificado.
     *
     * O livro do LivroUser também tem que ser o mesmo.
     * @param livro O Livro do usuário a ser atualizado
     * @throws BookinderException Se algo der errado ou as condições ali em cima não forem seguidas
     */
    public void update(LivroUser livro) throws BookinderException;
}
