package main;

import interfaces.Drawable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import abstractClasses.LockedToGrid;
import abstractClasses.UnlockedFromGrid;

@SuppressWarnings("serial")
public class GamePanel extends JPanel {

	private JFrame frame;
	private Map map;

	private int cameraX, cameraY;
	private double zoom;
	private final double MIN_ZOOM = .4, MAX_ZOOM = 2;

	public GamePanel(int width, int height, Map map) {

		this.map = map;
		
		setUpFrame(width, height);

		setUpCamera();
	}

	private void setUpCamera() {

		zoom = 1;
		
		System.out.println("Calc: " + map.numRows() * map.getTileSize());

//		cameraX = (map.numCols() * map.getTileSize()) - (getWidth() / 2);
//		cameraY = (map.numRows() * map.getTileSize()) - (getHeight() / 2);

		zoom = MIN_ZOOM;
	}

	public void moveCamera(int deltaX, int deltaY) {
		cameraX += deltaX;
		cameraY += deltaY;
	}

	public void zoomCamera(double deltaZoom) {
		zoom += deltaZoom;

		zoom = (zoom < MIN_ZOOM) ? MIN_ZOOM : zoom;

		zoom = (zoom > MAX_ZOOM) ? MAX_ZOOM : zoom;
	}

	private void setUpFrame(int width, int height) {
		frame = new JFrame();

		frame.add(this);

		setPreferredSize(new Dimension(width, height));

		frame.setResizable(false);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.pack();

		frame.setVisible(true);
	}

	public void update() {
		for (LockedToGrid[] row : map.getGrid()) {
			for (LockedToGrid cur : row) {
				cur.update();
			}
		}

		for (UnlockedFromGrid cur : map.getUnlockedObjects()) {
			cur.update();
		}
	}

	public JFrame getFrame() {
		return frame;
	}

	@Override
	public void paintComponent(Graphics g) {

		System.out.println(cameraX + " " + cameraY);

		System.out.println(zoom);

		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.BLACK);

		g2d.fillRect(0, 0, getWidth(), getHeight());

		g2d.translate(getWidth() / 2, getHeight() / 2);

		g2d.scale(zoom, zoom);

		g2d.translate(-getWidth() / 2, -getHeight() / 2);

		g2d.translate(-cameraX, -cameraY);

		g2d.translate(getWidth() / 2, getHeight() / 2);

		for (LockedToGrid[] row : map.getGrid()) {
			for (LockedToGrid cur : row) {
				cur.draw(g2d);
			}
		}

		for (UnlockedFromGrid cur : map.getUnlockedObjects()) {
			cur.draw(g2d);
		}
	}
}