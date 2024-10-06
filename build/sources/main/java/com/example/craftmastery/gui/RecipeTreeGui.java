package com.example.craftmastery.gui;

import com.example.craftmastery.CraftMastery;
import com.example.craftmastery.crafting.CraftingManager;
import com.example.craftmastery.crafting.RecipeWrapper;
import com.example.craftmastery.progression.PlayerProgression;
import com.example.craftmastery.progression.ProgressionManager;
import com.example.craftmastery.network.NetworkHandler;
import com.example.craftmastery.network.messages.UnlockRecipeMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

public class RecipeTreeGui extends GuiScreen {
    private static final ResourceLocation TEXTURE = new ResourceLocation(CraftMastery.MODID, "textures/gui/recipe_tree.png");
    private static final int GUI_WIDTH = 256;
    private static final int GUI_HEIGHT = 200;

    private final EntityPlayer player;
    private final PlayerProgression progression;
    private final List<RecipeNode> recipeNodes;
    private GuiButton backButton;
    private int guiLeft;
    private int guiTop;
    private int scrollOffset = 0;

    public RecipeTreeGui(EntityPlayer player) {
        this.player = player;
        this.progression = ProgressionManager.getInstance().getPlayerProgression(player);
        this.recipeNodes = buildRecipeTree();
    }

    private List<RecipeNode> buildRecipeTree() {
        List<RecipeNode> nodes = new ArrayList<>();
        Map<String, RecipeNode> nodeMap = new HashMap<>();

        for (RecipeWrapper recipe : CraftingManager.getInstance().getAllRecipes()) {
            RecipeNode node = new RecipeNode(recipe);
            nodes.add(node);
            nodeMap.put(recipe.getId(), node);
        }

        for (RecipeNode node : nodes) {
            for (String prerequisiteId : node.recipe.getPrerequisites()) {
                RecipeNode prerequisiteNode = nodeMap.get(prerequisiteId);
                if (prerequisiteNode != null) {
                    node.parents.add(prerequisiteNode);
                    prerequisiteNode.children.add(node);
                }
            }
        }

        return nodes;
    }

    @Override
    public void initGui() {
        super.initGui();
        guiLeft = (this.width - GUI_WIDTH) / 2;
        guiTop = (this.height - GUI_HEIGHT) / 2;

        backButton = new GuiButton(0, guiLeft + 10, guiTop + GUI_HEIGHT - 30, 100, 20, I18n.format("gui.craftmastery.back"));
        this.buttonList.add(backButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, GUI_WIDTH, GUI_HEIGHT);

        String title = I18n.format("gui.craftmastery.recipe_tree");
        this.fontRenderer.drawString(title, guiLeft + (GUI_WIDTH - this.fontRenderer.getStringWidth(title)) / 2, guiTop + 10, 0x404040);

        GlStateManager.pushMatrix();
        GlStateManager.translate(guiLeft + 10, guiTop + 30 - scrollOffset, 0);
        drawRecipeTree(mouseX - guiLeft - 10, mouseY - guiTop - 30 + scrollOffset);
        GlStateManager.popMatrix();

        super.drawScreen(mouseX, mouseY, partialTicks);

        drawTooltips(mouseX, mouseY);
    }

    private void drawRecipeTree(int mouseX, int mouseY) {
        for (RecipeNode node : recipeNodes) {
            drawRecipeNode(node, 0, 0);
        }
    }

    private void drawRecipeNode(RecipeNode node, int x, int y) {
        ItemStack output = node.recipe.getRecipe().getRecipeOutput();
        this.itemRender.renderItemAndEffectIntoGUI(output, x, y);

        boolean unlocked = progression.isRecipeUnlocked(node.recipe.getId());
        boolean canUnlock = progression.canUnlockRecipe(node.recipe);

        int color = unlocked ? 0x00FF00 : (canUnlock ? 0xFFFF00 : 0xFF0000);
        drawRect(x - 1, y - 1, x + 17, y + 17, color);

        for (RecipeNode child : node.children) {
            int childX = x + 30;
            int childY = y;
            drawLine(x + 16, y + 8, childX, childY + 8, 0xFFFFFF);
            drawRecipeNode(child, childX, childY);
        }
    }

    private void drawLine(int x1, int y1, int x2, int y2, int color) {
        GlStateManager.disableTexture2D();
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.color((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, 1.0F);
        GlStateManager.glBegin(1);
        GlStateManager.glVertex3f(x1, y1, 0);
        GlStateManager.glVertex3f(x2, y2, 0);
        GlStateManager.glEnd();
        GlStateManager.enableTexture2D();
    }

    private void drawTooltips(int mouseX, int mouseY) {
        for (RecipeNode node : recipeNodes) {
            if (isMouseOverNode(node, mouseX, mouseY)) {
                List<String> tooltip = new ArrayList<>();
                tooltip.add(node.recipe.getRecipe().getRecipeOutput().getDisplayName());
                tooltip.add(I18n.format("gui.craftmastery.required_level", node.recipe.getRequiredLevel()));
                tooltip.add(I18n.format("gui.craftmastery.point_cost", node.recipe.getPointCost()));
                this.drawHoveringText(tooltip, mouseX, mouseY);
                break;
            }
        }
    }

    private boolean isMouseOverNode(RecipeNode node, int mouseX, int mouseY) {
        int x = guiLeft + 10;
        int y = guiTop + 30 - scrollOffset;
        return mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16;
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (RecipeNode node : recipeNodes) {
            if (isMouseOverNode(node, mouseX, mouseY)) {
                if (progression.canUnlockRecipe(node.recipe)) {
                    progression.unlockRecipe(node.recipe);
                    NetworkHandler.INSTANCE.sendToServer(new UnlockRecipeMessage(node.recipe.getId()));
                }
                break;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button == backButton) {
            mc.displayGuiScreen(new ProgressionGui(player));
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scrollAmount = net.minecraft.util.math.MathHelper.clamp(Mouse.getEventDWheel(), -1, 1);
        scrollOffset -= scrollAmount * 10;
        scrollOffset = Math.max(0, Math.min(scrollOffset, getMaxScroll()));
    }

    private int getMaxScroll() {
        int treeHeight = recipeNodes.size() * 20; // Примерная высота дерева
        return Math.max(0, treeHeight - (GUI_HEIGHT - 60));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private static class RecipeNode {
        RecipeWrapper recipe;
        List<RecipeNode> parents = new ArrayList<>();
        List<RecipeNode> children = new ArrayList<>();

        RecipeNode(RecipeWrapper recipe) {
            this.recipe = recipe;
        }
    }
}
