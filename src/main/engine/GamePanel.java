package main.engine;

import main.entity.Player;
import main.road.GameRace;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;



public class GamePanel extends JPanel implements Runnable{
    private static final int FPS = 60;
    private static final double TIME_PER_TICK = 1e9 / FPS; // Nanoseconds per frame

    private final int width, height;
    private final String title;
    private Display display;

    private Thread thread;
    private boolean running = false;

    private BufferStrategy bs;
    private Graphics2D graphics;
    private KeyHandler keyHandler;

    private Player player;
    private GameRace gameRace;


    // Teste para a troca de telas
    private static int gameState = 0;
    private final int titleState = 0;

    private static int gameMode = 0;
    // 0 -solo
    // 1 - multiplayer

    private Clip currentClip;//musica

    public static void setGameState(int gameState) {
        GamePanel.gameState = gameState;
    }

    public static int getGameState() {
        return gameState;
    }

    public int getGameMode() {
        return gameMode;
    }

    public int getTitleState() {
        return titleState;
    }

    private Ui ui;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
    /// ---
    ///
    public GamePanel(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
        display = new Display(width, height, title);
        display.canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                System.out.println("Mouse Position: " + mouseX + ", " + mouseY);

                switch (gameState) {
                    case 0:// acoes no inicio
                        if (ui.isStartButtonClicked(mouseX, mouseY)) {
                            System.out.println("Botão Start clicado! Indo para a seleção de modo de jogo.");
                            gameState = gameState + 1;
                        }
                        break;
                    case 1:// acoes no menu
                        if (ui.isSoloButtonClicked(mouseX, mouseY)) {
                            System.out.println("Botão Solo clicado! Indo para a seleção de modo de jogo.");
                            gameState = gameState + 1;
                        }
                        if(ui.isMultiplayerButtonClicked(mouseX, mouseY)) {
                            System.out.println("multiplayer");
                            gameState = gameState + 1;
                            gameMode += 1;
                        }
                        if(ui.isExitButtonClicked(mouseX, mouseY)) {
                            System.out.println("sair");
                            gameState = 0;
                        }
                        break;
                    case 2: // acoes em selecionar carro
                        ui.handleCarSelectionClick(mouseX, mouseY);
                        if(ui.isSelectButtonClicked(mouseX, mouseY)) {
                            if(gameMode == 0){
                                //setar carro o player 1;
                                System.out.println("start");
                                gameState = gameState + 1;
                            }else{
                                if(ui.getPlayerIndex() == 0) {
                                    ui.handleCarSelectionClick(mouseX, mouseY);
                                    ui.confirmPlayerSelection();
                                    //setar carro o player 1;
                                }else{
                                    System.out.println("start");
                                    //setar carro do player 2
                                    gameState = gameState + 1;
                                }
                            }
                        }
                        break;
                    case 3:
                        switch (ui.handleMapSelectionClick(mouseX, mouseY)) {
                            case 1:
                                System.out.println("Rio");
                                // Mapa selecionado = Rio
                                tocarMusicaEmBackground("garotaDeIpanema"); // Toca a música correspondente
                                gameState = gameState + 1;
                                break;
                            case 2:
                                System.out.println("Caatinga");
                                // Mapa selecionado = Caatinga
                                tocarMusicaEmBackground("asaBranca"); // Toca Asa Branca
                                gameState = gameState + 1;
                                break;
                            case 3:
                                System.out.println("Amazonia");
                                // Mapa selecionado = Amazonia
                                tocarMusicaEmBackground("hakunaMatata"); // Toca Hakuna Matata
                                gameState = gameState + 1;
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }


            }
        });
    }

    private void init() throws IOException {
        keyHandler = new KeyHandler();
        display.getFrame().addKeyListener(keyHandler);
        display.getFrame().requestFocusInWindow();
        player = new Player(keyHandler);
        ui = new Ui(keyHandler);
        gameRace = new GameRace(width, height, title, player); // Inicializa o GameRace
        gameRace.init(); // Inicializa o GameRace
        tocarMusicaEmBackground("garotaDeIpanema");
    }

    private void tick() {
        if (gameState == 4) { // Verifica se está na corrida
            gameRace.update(); // Atualiza a lógica do GameRace
        }
        player.update();
    }

    private void render() {
        if (bs == null) {
            display.canvas.createBufferStrategy(3);
            bs = display.canvas.getBufferStrategy();
            return;
        }

        graphics = (Graphics2D) bs.getDrawGraphics();
        try {
            graphics.clearRect(0, 0, width, height);

            switch (gameState){
                case 0:
                    ui.drawMenu(graphics, this); // Tela Inicial
                    break;
                case 1:
                    ui.drawChooseGameMode(graphics, this);  // Escolha do modo de jogo
                    break;
                case 2:
                    if(gameMode == 0){
                        ui.drawSoloMode(graphics, this);
                    }else {
                        ui.drawMultiplayerMode(graphics, this);
                    }
                    break;
                case 3:
                    ui.drawSelectMapMode(graphics, this);
                    break;
                case 4:
                    gameRace.render(graphics);
                    display.getFrame().requestFocusInWindow();
                    break;

                default:
                    break;
            }
        } finally {
            graphics.dispose();
        }
        bs.show();
    }

    @Override
    public void run() {
        try {
            init();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        long lastTime = System.nanoTime();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / TIME_PER_TICK;
            lastTime = now;

            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }

        stop();
    }

    public synchronized void start() {
        if (running) return;
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        if (!running) return;
        running = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Metodo para parar a música atual
    public void pararMusica() {
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();  // Para o som
            currentClip.close(); // Libera os recursos
        }
    }

    // Metodo para tocar a nova música, parando a anterior
    public void tocarMusicaEmBackground(String song) {
        pararMusica(); // Para a música anterior, se estiver tocando
        new Thread(() -> som(song)).start();  // Toca a nova música em uma thread
    }

    // Modifica a função 'som' para usar 'currentClip'
    public void som(String song) {
        String file = "res/songs/" + song + ".wav"; // Caminho da música
        File arquivoAudio = new File(file);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivoAudio);
            currentClip = AudioSystem.getClip();  // Atribui o novo Clip à variável 'currentClip'
            currentClip.open(audioInputStream);

            // Toca o som em loop contínuo
            currentClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Display getDisplay() {
        return display;
    }
}