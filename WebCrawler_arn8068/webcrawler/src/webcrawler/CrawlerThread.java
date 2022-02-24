package webcrawler;

import java.net.*;
import java.io.*;
import java.util.Vector;

import threads.*;

public class CrawlerThread extends ControllableThread {
	public void process(Object o) {

		try {
			URL pageURL = (URL) o;
			// See if it's a jpeg, mpeg or avi

			String filename = pageURL.getFile().toLowerCase();
			if (filename.endsWith(".jpg") || filename.endsWith(".jpeg")|| filename.endsWith(".mpeg")||
					filename.endsWith(".mpg") || filename.endsWith(".avi") || filename.endsWith(".wmv")) {
				
				filename = filename.replace('/', '-');
				filename = ((URLQueue) queue).getFilenamePrefix() + pageURL.getHost() + filename;
				
				System.out.println("Saving to file " + filename);
				try {
					SaveURL.writeURLtoFile(pageURL, filename);
				} catch (Exception e) {
					System.out.println("Saving to file " + filename + " from URL " + 
							pageURL.toString() + " failed due to a " + e.toString());
				}
				return;
			}

 			String mimetype = pageURL.openConnection().getContentType();
            if (!mimetype.startsWith("text")) return;

			String rawPage = SaveURL.getURL(pageURL);
            String smallPage = rawPage.toLowerCase().replaceAll("\\s", " ");

			Vector links = SaveURL.extractLinks(rawPage, smallPage);

			for (int n = 0; n < links.size(); n++) {
				try {

					URL link = new URL(pageURL, (String) links.elementAt(n));
					if (tc.getMaxLevel() == -1)
						queue.push(link, level);
					else
						queue.push(link, level + 1);
				} catch (Exception e) {

				}
			}
		} catch (Exception e) {

		}
	}
}
