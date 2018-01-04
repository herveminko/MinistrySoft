/**
 *
 */
package jw.ministry.soft.modules.utils;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.Chart;
import javafx.scene.image.WritableImage;

/**
 * @author HervéClaude
 *
 */
public class GraphicsUtils {

	/**
	 * Save a FX XY Chart to an image file
	 *
	 * @param chart
	 *            is the fx chart to save as image.
	 * @param path
	 *            is the path (including file name) where to save the image
	 * @param format
	 *            is the image file format.
	 */
	public static void exportFxChartAsImage(Chart chart, String path, String format) {

		WritableImage image = chart.snapshot(new SnapshotParameters(), null);
		File file = new File(path + "." + format);
		if (image != null) {
			try {
				ImageIO.write(SwingFXUtils.fromFXImage(image, null), format, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}


	}

}
