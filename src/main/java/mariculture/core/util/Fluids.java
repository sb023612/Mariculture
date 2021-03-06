package mariculture.core.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class Fluids {
    public static Fluids instance = new Fluids();

    static HashMap<String, Fluid> fluids = new HashMap();

    public void addFluid(String name, Fluid fluid) {
        fluids.put(name, fluid);
    }

    public static FluidStack getStack(String name, int amount) {
        return FluidRegistry.getFluidStack(name, amount);
    }

    public static Fluid getFluid(String name) {
        for (Map.Entry<String, Fluid> entry : fluids.entrySet())
            if (entry.getKey().equalsIgnoreCase(name)) return entry.getValue();

        return FluidRegistry.WATER;
    }

    public static boolean exists(String string) {
        return FluidRegistry.getFluid(string) != null;
    }

    public boolean fluidExists(String name) {
        for (Map.Entry<String, Fluid> entry : fluids.entrySet())
            if (entry.getKey().equalsIgnoreCase(name)) return true;

        if (name.equals("aluminum")) if (checkInFluidRegistry("aluminum")) return true;

        return checkInFluidRegistry(name);
    }

    public boolean checkInFluidRegistry(String name) {
        if (FluidRegistry.isFluidRegistered("molten" + name)) {
            addFluid(name, FluidRegistry.getFluid("molten" + name));
            return true;
        } else if (FluidRegistry.isFluidRegistered("molten " + name)) {
            addFluid(name, FluidRegistry.getFluid("molten " + name));
            return true;
        }

        return false;
    }

    //Dirt
    public static String dirt;

    //Metals and Glass
    public static String aluminum;
    public static String titanium;
    public static String iron;
    public static String gold;
    public static String copper;
    public static String tin;
    public static String magnesium;
    public static String bronze;
    public static String lead;
    public static String silver;
    public static String steel;
    public static String nickel;
    public static String glass;
    public static String rutile;
    //Other Metals
    public static String electrum;

    //Can end up being left as null if not registered by TiC
    public static String cobalt;

    //Other
    public static String fish_food;
    public static String fish_oil;
    public static String natural_gas;
    public static String quicklime;
    public static String salt;
    public static String milk;
    public static String custard;

    //Block forms
    public static String hp_water = "fastwater";
}
