package framework.states;

import framework.entities.Block;
import framework.entities.Food;
import framework.entities.Snake;
import java.awt.Graphics;
import java.awt.Color;
import static framework.Frame.SCALED_HEIGHT;
import static framework.Frame.SCALED_WIDTH;

//Estado em que o se joga, com a cobra, comida, pontos, tempo
//Ver proximo: Snake.java, Food.java
public class PlayingState extends State {
    private int score = 0; //Pontos
    private int timer = 0; //Tempo
    private Snake snake; //Cobra
    private Food food; //Comida
    private ScoreState nextState; //Estado de fim de jogo
    //Inicializa o estado
    public PlayingState(String name, StateMachine stateMachine) {
        super(name, stateMachine); //Chama o construtor mae
        snake = new Snake(stateMachine.getComponent()); //Inicializa a cobra
        food = new Food(); //Inicializa a comida
        nextState = new ScoreState(stateMachine, snake, this); // Inicializa o estado de fim de jogo
        setNextState(nextState); //Define o estado de fim de jogo como o proximo na sequencia
        //Esse metodo setNextState vem da classe mae, State.java
    }

    //Como vai ser renderizado esse estado na tela
    //Implementando metodo render do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void render(Graphics g) {
        g.setFont(titleFont); //Define a fonte (titleFont vem da classe State.java)
        g.setColor(Color.WHITE); //Define a cor
        //Usando a fonte e cor definida, escreve os pontos no centro da tela, no y = 60
        g.drawString(String.valueOf(score), SCALED_WIDTH/2 -  g.getFontMetrics(titleFont).stringWidth(String.valueOf(score)) /2, 60);
        //Usando a fonte e cor definida, escreve o tempo na tela
        g.drawString(String.valueOf(timer/60), SCALED_WIDTH - g.getFontMetrics(titleFont).stringWidth(String.valueOf(timer/60)) - 10, SCALED_HEIGHT);
        snake.render(g); //renderiza a cobra
        food.render(g); //renderiza a comida
    }

    //Como os valores desse estado vao ser atualizados
    //Implementando metodo tick do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void tick() {
        snake.tick(); //atualiza a cobra

        //Se a comida for coletada pela cobra
        if (food.isCollected(snake)) {
            score++; //aumenta os pontos
            food = new Food(); //Aqui ta reinstanciando o objeto, mas na pratica muda a posicao da comida
            //Adiciona mais um bloco ao rabo da cobra
            Snake.getBlocks().add(new Block(Snake.getBlocks().get(Snake.getBlocks().size()-1), snake.getLastPressed()));
        }
        //Se a cobra colidir em si mesma
        if (snake.isCollided()) {
            loadNextState(); //Carrega o proximo estado, que no caso é o game over (ScoreState nextState)
            snake.stop(); // para a cobra
        }
        timer++; //aumenta o tempo
    }

    public int getScore() {
        return score;
    } //Retorna o valor dos pontos
    public int getTimer() {
        return timer;
    } //Retorna o valor do tempo
    public void resetTimer() {
        timer = 0;
    } //Reinicia o tempo
    public void resetScore() {
        score = 0;
    } //Reinicia os pontos
}
