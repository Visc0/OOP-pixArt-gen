package it.unibo.pixArt.model.tool.fillTools;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.unibo.pixArt.model.pixel.ImplPixel;
import it.unibo.pixArt.model.pixel.Pixel;
import it.unibo.pixArt.model.tool.AbstractTool;
import javafx.scene.paint.Color;

public class Bucket extends AbstractTool{

    private final Color selectedColor;

    public Bucket(final Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    @Override
    public Set<Pixel> updateGrid(final Pixel pixel, Set<Pixel> frame) {
        Map<Pixel, Pixel> frameMap = getFrameMap(frame);
        Set<Pixel> newSet = new HashSet<>();
        Color old_color = pixel.getColor();

        if (old_color.equals(this.selectedColor)) {
            return Collections.emptySet(); 
        }
        
        Queue<Pixel> queue = new LinkedList<>();
        queue.add(pixel);

        while (!queue.isEmpty()) {
            Pixel temp = queue.poll();
            if (!isValid(frameMap, temp.getPosition().getX(), temp.getPosition().getY(), old_color, selectedColor)) {
                continue;
            }
            else {
                temp.setColor(selectedColor);
                newSet.add(temp);
                queue.add(frameMap.get(new ImplPixel(temp.getPosition().getX()+1, temp.getPosition().getY())));
                queue.add(frameMap.get(new ImplPixel(temp.getPosition().getX()-1, temp.getPosition().getY())));
                queue.add(frameMap.get(new ImplPixel(temp.getPosition().getX(), temp.getPosition().getY()+1)));
                queue.add(frameMap.get(new ImplPixel(temp.getPosition().getX(), temp.getPosition().getY()-1)));
            }
        }

        return newSet;
    }

    private boolean isValid(final Map<Pixel, Pixel> frame, final int x, final int y, final Color oldColor, final Color newColor) {
        if (x < 0 || x >= frame.size() || y < 0 || y >= frame.size() || frame.get(new ImplPixel(x, y)).getColor() != oldColor 
            || frame.get(new ImplPixel(x, y)).getColor() == newColor) {
            return false;
        }  
        return true;
    }

    private Map<Pixel, Pixel> getFrameMap(Set<Pixel> frame) {
        return frame.stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
    }
}

