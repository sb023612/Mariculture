package mariculture.core.render;

import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.lwjgl.opengl.GL11;

public abstract class RenderBase {
	public static HashMap<String, RenderBase> renderers = new HashMap();
	
	static final double RENDER_OFFSET = 0.0010000000474974513D;
	static final float LIGHT_Y_NEG = 0.5F;
	static final float LIGHT_Y_POS = 1.0F;
	static final float LIGHT_XZ_NEG = 0.8F;
	static final float LIGHT_XZ_POS = 0.6F;
	
	public RenderBlocks render;
	public ForgeDirection dir = ForgeDirection.UNKNOWN;
	public IBlockAccess world;
	public int x, y, z;
	public IIcon icon;
	public Block block;
	
	public RenderBase(RenderBlocks render) {
		this.render = render;
	}
	
	public RenderBase setCoords(IBlockAccess world, int x, int y, int z) {
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		this.block = world.getBlock(x, y, z);
		return this;
	}
	
	public RenderBase setDir(ForgeDirection dir) {
		this.dir = dir;
		return this;
	}
	
	public RenderBase setBlock(Block block) {
		this.block = block;
		return this;
	}
	
	public boolean render() {
		if(isItem()) {
			GL11.glPushMatrix();
			GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
	        GL11.glTranslatef(-0.5F, -0.4F, -0.5F);
			GL11.glTranslatef(0F, -0.1F, 0F);
			renderBlock();
			GL11.glPopMatrix();
		} else {
			renderBlock();
			if(render.hasOverrideBlockTexture())
				render.clearOverrideBlockTexture();
		}
		
		return true;
	}
	
	public abstract void renderBlock();
	
	public boolean isItem() {
		return world == null;
	}
	
	protected void setTexture(IIcon texture) {
		if(isItem())
			icon = texture;
		else
			render.setOverrideBlockTexture(texture);
	}
	
	protected void setTexture(Block block, int meta) {
		setTexture(block.getIcon(0, meta));
	}
	
	protected void setTexture(Block block) {
		setTexture(block, 0);
	}
	
	protected void renderBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		if(isItem())
			renderItemBlock(minX, minY, minZ, maxX, maxY, maxZ);
		else
			renderWorldBlock(minX, minY, minZ, maxX,  maxY, maxZ);
	}
	
	protected void renderFluid(FluidStack fluid, int max, double scale, int xPlus, int yPlus, int zPlus) {
		int x2 = x + xPlus;
		int y2 = y + yPlus;
		int z2 = z + zPlus;
		
		Tessellator tessellator = Tessellator.instance;
		int color = block.colorMultiplier(world, x2, y2, z2);
		float red = (color >> 16 & 255) / 255.0F;
		float green = (color >> 8 & 255) / 255.0F;
		float blue = (color & 255) / 255.0F;
		
		double extra = (((double)fluid.amount)/max) * scale;
		double height = 0.4D + extra;
		IIcon iconStill = fluid.getFluid().getIcon();

		height += RENDER_OFFSET;

		double u1, u2, u3, u4, v1, v2, v3, v4;
		u2 = iconStill.getInterpolatedU(0.0D);
		v2 = iconStill.getInterpolatedV(0.0D);
		u1 = u2;
		v1 = iconStill.getInterpolatedV(16.0D);
		u4 = iconStill.getInterpolatedU(16.0D);
		v4 = v1;
		u3 = u4;
		v3 = v2;

		tessellator.setBrightness(200);
		tessellator.setColorOpaque_F(LIGHT_Y_POS * red, LIGHT_Y_POS * green, LIGHT_Y_POS * blue);
		tessellator.addVertexWithUV(x2 + 0, y2 + height, z2 + 0, u2, v2);
		tessellator.addVertexWithUV(x2 + 0, y2 + height, z2 + 1, u1, v1);
		tessellator.addVertexWithUV(x2 + 1, y2 + height, z2 + 1, u4, v4);
		tessellator.addVertexWithUV(x2 + 1, y2 + height, z2 + 0, u3, v3);

		render.renderMinY = 0;
		render.renderMaxY = 1;
	}
	
	protected void renderFluidBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		render.renderAllFaces = true;
		render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
		render.renderStandardBlock(Blocks.lava, this.x, this.y, this.z);
		render.renderAllFaces = false;
	}

	private void renderWorldBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		render.renderAllFaces = true;
		render.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
		render.renderStandardBlock(block, this.x, this.y, this.z);
		render.renderAllFaces = false;
	}

	private void renderItemBlock(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		render.renderMinX = minX;
	    render.renderMinY = minY;
	    render.renderMinZ = minZ;
	    render.renderMaxX = maxX;
	    render.renderMaxY = maxY;
	    render.renderMaxZ = maxZ;
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, -1.0F, 0.0F);
	    render.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 1.0F, 0.0F);
	    render.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, -1.0F);
	    render.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(0.0F, 0.0F, 1.0F);
	    render.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(-1.0F, 0.0F, 0.0F);
	    render.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	    tessellator.startDrawingQuads();
	    tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    render.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, icon);
	    tessellator.draw();
	}
}
