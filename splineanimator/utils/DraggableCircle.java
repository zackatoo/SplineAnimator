package com.zackatoo.splineanimator.utils;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

public class DraggableCircle
{
    private Circle node;
    private double startX = 0;
    private double startY = 0;
    private int prevX = 0;
    private int prevY = 0;
    private DragUpdate dragUpdate = null;
    private boolean dragging = false;

    public DraggableCircle(Circle node)
    {
        this.node = node;
        node.setOnMouseEntered(event -> node.setCursor(Cursor.HAND));
        node.setOnMousePressed(this::startDrag);
        node.setOnMouseDragged(this::inDrag);
        node.setOnMouseReleased(this::endDrag);
    }

    private void startDrag(MouseEvent event)
    {
        node.setCursor(Cursor.CLOSED_HAND);
        dragging = true;
    }

    private void inDrag(MouseEvent event)
    {
        if (!dragging) return;

        if (event.getX() != prevX || event.getY() != prevY)
        {
            node.setCenterX(event.getX());
            node.setCenterY(event.getY());
            prevX = (int)event.getX();
            prevY = (int)event.getY();

            if (dragUpdate != null)
            {
                dragUpdate.update(prevX, prevY);
            }
        }
    }

    private void endDrag(MouseEvent event)
    {
        node.setCursor(Cursor.HAND);
        dragging = false;
    }

    public DragUpdate getDragUpdate()
    {
        return dragUpdate;
    }

    public void setDragUpdate(DragUpdate dragUpdate)
    {
        this.dragUpdate = dragUpdate;
    }
}
