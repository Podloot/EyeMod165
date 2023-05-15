package com.podloot.eyemod.gui.elements.map;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.podloot.eyemod.lib.gui.EyeDraw;
import com.podloot.eyemod.lib.gui.util.Image;
import com.podloot.eyemod.lib.gui.util.Line;
import com.podloot.eyemod.lib.gui.widgets.EyeWidget;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;

public class MapItem extends EyeWidget {

	BlockPos pos;
	String name;
	int rotation = 180;
	Image icon;
	int color;
	int off = 0;
	
	float ps = 0F;
	
	public MapItem(Image icon, BlockPos pos, int color) {
		super(false, 8, 8);
		this.icon = icon;
		this.pos = pos;
		this.color = color;	
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void draw(MatrixStack matrices, int x, int y) {
		matrices.pushPose();
		matrices.translate(x, y-off, 0);
        matrices.translate(ps, ps, 0);
        matrices.mulPose(Vector3f.ZP.rotationDegrees(rotation+180));
        matrices.translate(-icon.width/2, -icon.height/2, 0);
        EyeDraw.texture(matrices, icon, 0, 0, 12, 12, color);
		matrices.popPose();
		
		if(name != null && !name.isEmpty()) {
			EyeDraw.text(matrices, new Line(name).setScale(0.6F), (int)(x + (2) + ps), (int)(y + 4 + ps));
		}
		
		
	}
	
	public void updatePos(int width, int height, int mapSize, BlockPos mapPos, float ps) {
		float nx = ((float)width / (float)mapSize) * (float)(pos.getX() - mapPos.getX() + mapSize/2F);
		float nz = ((float)height / (float)mapSize) * (float)(pos.getZ() - mapPos.getZ() + mapSize/2F);
		this.setPos((int)nx, (int)nz);
		this.ps = ps;
	}
	
	public void setBlockPos(BlockPos pos) {
		this.pos = pos;
	}
	
	public void setRotation(int rotation) {
		this.rotation = rotation;
	}
	
	public BlockPos getPos() {
		return pos;
	}

}
