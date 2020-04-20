package framework;

import framework.states.StateMachine;
import framework.states.TitleScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

//Esta classe é um canvas e contém o gameloop. Tudo é atualizado aqui
//Ver Proximo: StateMachine.java
public class Frame extends Canvas implements Runnable {

    public static JFrame frame; // A propria janela
    private StateMachine stateMachine; //Gerenciador de estados (telas)
    private TitleScreen titleScreen; //Estado inicial, tela com o título
    private static final int WIDTH = 160;
    private static final int HEIGHT = 120;
    private static final int SCALE = 4;
    public static final int SCALED_WIDTH = WIDTH*SCALE; //largura
    public static final int SCALED_HEIGHT = HEIGHT*SCALE; //altura

    private boolean running;

    //Instancia a janela
    public Frame() {
        initFrame(); //Faz as coisas padrao de janela, tamanho, posicao etc.
        stateMachine = new StateMachine(this); //Inicializa o gerenciador de esatdos
        titleScreen = new TitleScreen(stateMachine); // Inicializa a tela inicial e adiciona o stateMachine como o gerenciador dela
        stateMachine.setCurrentState(titleScreen); // Define o estado atual como a tela inicial
    }

    private void initFrame() {
        this.setPreferredSize(new Dimension(SCALED_WIDTH, SCALED_HEIGHT)); //Define o tamanho da janela
        frame = new JFrame("SNAKE"); // Titulo, aparece no canto de cima
        frame.add(this); //Adiciona o canvas (essa classe) na janela
        frame.setResizable(false); //Nao deixa redimensionar a janela
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Funcao do botao X
        frame.setVisible(true);
        frame.pack();
        frame.setLocationRelativeTo(null); // Coloca no meio
    }

    //Inicializa o gameloop pra rodar o jogo
    public synchronized void start() {
        Thread thread = new Thread(this);
        thread.start();
        running = true;
    }

    //Atualiza todos as variaveis do jogo
    public void tick() {
        requestFocus(); //Isso é pra poder pegar eventos do teclado
        stateMachine.getCurrentState().tick();
        //Chama o metodo tick do estado atual
        //O estado atual quando inicializa a aplicacao sempre é o TitleScreen
    }
    //Renderiza tudo pra ser visivel na tela
    public void render(){
        // Cria a imagem em que tudo vai ser renderizado
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        //Graphics é a classe que manipula a renderizacao
        Graphics g = bs.getDrawGraphics();
        //Chama o metodo de renderizar do estado atual, no inicio sempre é o TitleScreen
        stateMachine.getCurrentState().render(g);
        bs.show(); // Mostra na tela tudo que foi renderizado

        //Pinta a tela de preto denovo, isso é pra apagar o render passado
        g.setColor(Color.BLACK);
        g.fillRect(0,0,SCALED_WIDTH, SCALED_HEIGHT);
        //Sem isso, os objetos sao duplicados na tela
    }

    //Game loop pra atualizar e renderizar tudo 60 vezes por segundo
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        int frames = 0;
        double timer = System.currentTimeMillis();
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                tick(); //atualiza tudo
                render(); //renderiza tudo com os valores atualizados
                frames ++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >= 1000) {
                System.out.println("FPS: " + frames);
                frames = 0;
                timer+=1000;
                System.out.println(stateMachine.getCurrentState().toString());
                //Te diz em que estado o jogo está atualmente
            }
        }
    }
}
