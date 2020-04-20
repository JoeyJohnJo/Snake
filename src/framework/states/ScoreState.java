package framework.states;

import framework.KeyHandler;
import framework.entities.Snake;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;
import static framework.Frame.SCALED_HEIGHT;
import static framework.Frame.SCALED_WIDTH;
//Estado do game over, só pausa tudo e mostra na tela
public class ScoreState extends State{
    private KeyHandler keyHandler; //Listener do teclado
    private PlayingState playingState; //Estado do jogo em si, pra ser recarregado
    private Snake snake; //Instancia da cobra pra parar e reiniciar o movimento dela

    //Inicializa o estado
    public ScoreState(StateMachine stateMachine, Snake snake, PlayingState playingState) {
        super("ScoreState", stateMachine);
        keyHandler = new KeyHandler(stateMachine.getComponent()); //Inicializa o listener pra poder usar o teclado
        this.snake = snake;
        this.playingState = playingState;
    }

    //Como vai ser renderizado esse estado na tela
    //Implementando metodo render do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void render(Graphics g) {
        g.setFont(titleFont); //Define a fonte (titleFont vem da classe State.java)
        g.setColor(Color.WHITE); //Define a cor
        //Escreve os pontos no centro da tela no y = 60
        g.drawString(String.valueOf(playingState.getScore()),
                SCALED_WIDTH/2 -  g.getFontMetrics(titleFont).stringWidth(String.valueOf(playingState.getScore() )) /2, 60);

        g.setFont(subFont); //Muda o tamanho da fonte pra 16
        g.setColor(Color.WHITE); //Define a cor como branco
        //Escreve GAME OVER no meio da tela
        g.drawString("GAME OVER", SCALED_WIDTH/2 - g.getFontMetrics(titleFont.deriveFont(16f)).stringWidth("GAME OVER")/2, 250);
        g.setFont(titleFont); //Volta pra fonte grande
        //Escreve o tempo no canto de baixo da tela
        g.drawString(String.valueOf(playingState.getTimer()/60), SCALED_WIDTH - g.getFontMetrics(titleFont).stringWidth(String.valueOf(playingState.getTimer()/60)) - 10, SCALED_HEIGHT);
    }

    //Como os valores desse estado vao ser atualizados
    //Implementando metodo tick do State.java que estava sem implementação
    //Lembrando que tick e render rodam 60 vezes por segundo
    @Override
    public void tick() {
        //unica coisa que esse estado faz é voltar pro estado do jogo quando aperta espaço
        if (keyHandler.getPressedKeys().contains(KeyEvent.VK_SPACE)) {
            /*Define o proximo estado como o estado anterior do gerenciador,
            * que no caso é o estado do jogo em si, ent só volta pra la*/
            setNextState(stateMachine.getPreviousState());
            loadNextState(); //Carrega o estado do jogo
            playingState.resetTimer(); //Reinicia o tempo
            playingState.resetScore(); //Reinicia os pontos
            snake.restart(); //Reinicia a cobra
        }
    }
}
