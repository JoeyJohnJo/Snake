package framework.entities;

import framework.Frame;
import framework.KeyHandler;
import java.awt.Graphics;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

//A cobra, normalmente esta classe daria extends numa classe entity, mas como é um jogo pequeno é desnecessario
//Ver próximo: Block.java
public class Snake {
    private KeyHandler keyHandler; // O listener pra receber eventos de teclas
    private int lastPressedKey; // Ultima tecla pressionado, é um int pq as teclas são representadas por seus codigos
    private int frames = 0; //Pra mudar a velocidade de movimento
    private boolean collided = false; //Para saber se colidiu com o proprio rabo ou nao
    private Block head = new Block(); //A cabeça da cobra, ja que é só isso que controlamos
    private static ArrayList<Block> trailing = new ArrayList<>(); //Lista dos blocos que formam o corpo
    public Snake(Component c) {
        this.keyHandler = new KeyHandler(c); //inicializa o listener de teclas
        lastPressedKey = KeyEvent.VK_RIGHT; //Define uma direçao inicial que ela deve estar andando
        //Pode ser qualquer direçao mas coloquei pra direita porque parece melhor
        trailing.add(head); //adiciona a cabeça na lista de blocos do corpo
        trailing.add(new Block(trailing.get(0), KeyEvent.VK_RIGHT));
        trailing.add(new Block(trailing.get(1), KeyEvent.VK_RIGHT));
        trailing.add(new Block(trailing.get(2), KeyEvent.VK_RIGHT));
        //Adiciona mais blocos no corpo, dando à cobra um tamanho incial
        //Sequiser maior adiciona mais, se quiser menor apaga algum bloco
    }

    //Atualiza as variaveis, lembrando que acontece 60 vezes por segundo
    public void tick() {
        getInput(); //Recebe a tecla apertada
        frames++; //Aumenta essa variavel pra entrar no if
        /*Ja que o if so ocorre se for maior que 2, mesmo que o tick seja chamado 60 vezes por segundo,
        *o que está aqui dentro so vai acontecer 30 vezes por segundo. Isso porque se deixar 60 vezes
        *por segundo a cobra fica muito rapida*/
        if (frames > 2) {
            frames = 0; //Reseta pra 0 pra proxima iteracao
            move(); //atualiza a posicso da cobra
            wrap(); //Se passar da tela, volta do outro lado
        }
    }
    //Renderiza a cobra na tela, lembrando que acontece 60 vezes por segundo
    public void render(Graphics g) {
        //Renderiza os blocos do corpo da cobra
        for(Block b : trailing) {
            b.render(g);
        }
    }
    //Muda a ultima tecla pressionada baseado em qual tecla direcional foi apertada
    private void getInput() {
        /*Como no jogo vc nao pode mudar de direcao opostas,
         por exemplo, diretamente da esquerda pra direita,
         tem que conferir primeiro se a tecla apertada antes nao é oposta a que foi apertada agora*/
        if (keyHandler.getPressedKeys().contains(KeyEvent.VK_UP)) {
            if (lastPressedKey != KeyEvent.VK_DOWN) lastPressedKey = KeyEvent.VK_UP;
        }
        else if (keyHandler.getPressedKeys().contains(KeyEvent.VK_DOWN)) {
            if (lastPressedKey != KeyEvent.VK_UP) lastPressedKey = KeyEvent.VK_DOWN;
        }
        else if (keyHandler.getPressedKeys().contains(KeyEvent.VK_LEFT)) {
            if (lastPressedKey != KeyEvent.VK_RIGHT) lastPressedKey = KeyEvent.VK_LEFT;
        }
        else if (keyHandler.getPressedKeys().contains(KeyEvent.VK_RIGHT)) {
            if (lastPressedKey != KeyEvent.VK_LEFT) lastPressedKey = KeyEvent.VK_RIGHT;
        }
    }

    //atualizar a posicao da cobra
    private void move() {
        //Se a ultima tecla apertada for a do case, muda a posicao adequadamente
        switch (lastPressedKey) {
            case KeyEvent.VK_UP:
                head.y-=head.getSpeed();
                break;
            case KeyEvent.VK_DOWN:
                head.y+=head.getSpeed();
                break;
            case KeyEvent.VK_LEFT:
                head.x-=head.getSpeed();
                break;
            case KeyEvent.VK_RIGHT:
                head.x+=head.getSpeed()  ;
                break;
        }
        head.updatePos(); //Atualiza os dados da cabeca

        //Pra todos os outros menos a cabeça
        for (Block b : trailing) {
            if (b != head) {
                if (trailing.get(trailing.indexOf(b)-1).getPreviousPos().size() > 0) {
                    b.setRow(trailing.get(trailing.indexOf(b) - 1).getPreviousPos().get(0)); //Defina a linha em que o bloco está
                    b.setColumn(trailing.get(trailing.indexOf(b) - 1).getPreviousPos().get(1)); //Define a coluna em que o bloco está
                    b.updatePos(); //Atualiza os dados do bloco

                    //Confere se nao está colidindo com a cabeça
                    if (b.isColliding(head))
                        collided = true;
                }
            }
        }
    }

    //Muda a posicao da cobra se passar da tela
    private void wrap () {
        if (head.x > Frame.SCALED_WIDTH)
            head.x = 0;
        else if (head.x < 0)
            head.x = Frame.SCALED_WIDTH;

        if (head.y > Frame.SCALED_HEIGHT)
            head.y = 0;
        else if (head.y < 0)
            head.y = Frame.SCALED_HEIGHT;
    }

    //Para a cobra, no estado do game over
    public void stop() {
        trailing.clear(); //Limpa os blocos do corpo, tirando o tamanho
        trailing.add(head); //Adiciona a cabeç apra ter só um bloco
        lastPressedKey = 0; //Coloca a ultima tecla pressionada pra uma sem funçao
    }
    //Reinicia a cobra, quando volta do game over pro jogo
    public void restart() {
        collided = false; //Volta pra falso ja que acabou de começar
        lastPressedKey = KeyEvent.VK_RIGHT; //Definindo a direçao incial de movimento
        trailing.add(new Block(trailing.get(0), KeyEvent.VK_RIGHT));
        trailing.add(new Block(trailing.get(1), KeyEvent.VK_RIGHT));
        trailing.add(new Block(trailing.get(2), KeyEvent.VK_RIGHT));
        //Adicionando os blocos de tamanho inicial da cobra
    }

    /////////////////GETS//////////////////////////////////////
    public static ArrayList<Block> getBlocks() {
        return trailing;
    }
    public Block getHead() {
        return trailing.get(0);
    }
    public int getLastPressed() {
        return lastPressedKey;
    }

    public boolean isCollided() {
        return collided;
    }
    //////////////////////////////////////////////////////////
}
