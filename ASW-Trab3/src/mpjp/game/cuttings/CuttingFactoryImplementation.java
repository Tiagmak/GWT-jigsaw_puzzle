package mpjp.game.cuttings;

import java.util.HashSet;
import java.util.Set;

import mpjp.shared.MPJPException;

public class CuttingFactoryImplementation implements CuttingFactory{

	public CuttingFactoryImplementation() {
		super();
	}

	/**
	 * Create a cutting with given name. Provided name must be one of given by CuttingFactory.getAvaliableCuttings()
	 * @param name - if cutting
	 * @return cutting -  if name is invalid or if an exception is raised while instantiating
	 */
	@Override
	public Cutting createCutting(String name) throws MPJPException {
		Cutting cutting;
		switch (name) {
		case "Round": {
			cutting = new RoundCutting();
			break;
		}
		case "Standard":{
			cutting = new StandardCutting();
			break;
		}
		case "Straight":{
			cutting = new StraightCutting();
			break;
		}
		case "Triangular":{
			cutting = new TriangularCutting();
			break;
		}
		default:
			throw new MPJPException("Invalid name");
		}
		return cutting;
	}

	/**
	 * Set of valid cutting names. They can be used as argument for CuttingFactory.createCutting(String)
	 * @return set of valid cutting names
	 */
	@Override
	public Set<String> getAvaliableCuttings() {
		Set<String> avaliableCuttings = new HashSet<String>();
		avaliableCuttings.add("Round");
		avaliableCuttings.add("Standard");
		avaliableCuttings.add("Straight");
		avaliableCuttings.add("Triangular");
		return avaliableCuttings;
	}
}
