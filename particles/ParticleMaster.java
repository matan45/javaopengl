package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import entities.Camera;
import maths.Matrix4f;
import renderer.Loader;

public class ParticleMaster {
	static Map<ParticleTexture, List<Particle>> particles = new HashMap<>();
	static ParticleRenderer renderer;

	public static void init(Loader loader, Matrix4f projectionMatrix) {
		renderer = new ParticleRenderer(loader, projectionMatrix);
	}

	public static void update(Camera camera) {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = particles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			Entry<ParticleTexture, List<Particle>> entry = mapIterator.next();
			List<Particle> list = entry.getValue();
			Iterator<Particle> iterator = list.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				boolean stillAlive = p.update(camera);
				if (!stillAlive) {
					iterator.remove();
					if (list.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
			if (!entry.getKey().isAdditive()) {
				InsertionSort.sortHighToLow(list);
			}
		}

	}

	public static void renderParticles(Camera camera) {
		renderer.render(particles, camera);
	}

	public static void cleanup() {
		renderer.cleanUp();
	}

	public static void addParticle(Particle particale) {
		List<Particle> list = particles.get(particale.getTexture());
		if (list == null) {
			list = new ArrayList<>();
			particles.put(particale.getTexture(), list);
		}
		list.add(particale);
	}
}
