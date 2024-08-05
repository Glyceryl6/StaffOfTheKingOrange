package com.glyceryl6.staff.client.gui;

import com.glyceryl6.staff.server.network.SetStaffCommandC2SPacket;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CommandSuggestions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@OnlyIn(Dist.CLIENT)
public class StaffCommandBlockEditScreen extends Screen {

    private static final Component SET_COMMAND_LABEL = Component.translatable("advMode.setCommand");
    private static final Component COMMAND_LABEL = Component.translatable("advMode.command");
    private String commandContent;
    protected EditBox commandEdit;
    protected Button doneButton;
    protected Button cancelButton;
    CommandSuggestions commandSuggestions;

    public StaffCommandBlockEditScreen() {
        super(GameNarrator.NO_TITLE);
    }

    @Override
    protected void init() {
        this.doneButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> this.onDone())
                .bounds(this.width / 2 - 4 - 150, this.height / 4 + 24, 150, 20).build());
        this.cancelButton = this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> this.onClose())
                .bounds(this.width / 2 + 4, this.height / 4 + 24, 150, 20).build());
        this.commandEdit = new EditBox(this.font, this.width / 2 - 150, 50, 300, 20, COMMAND_LABEL);
        this.commandEdit.setValue(this.commandContent);
        this.commandEdit.setMaxLength(Short.MAX_VALUE);
        this.commandEdit.setResponder(this::onEdited);
        this.addWidget(this.commandEdit);
        this.commandSuggestions = new CommandSuggestions(this.minecraft, (this), this.commandEdit,
                this.font, Boolean.TRUE, Boolean.TRUE, (0), (7), Boolean.FALSE, Integer.MIN_VALUE);
        this.commandSuggestions.setAllowSuggestions(true);
        this.commandSuggestions.updateCommandInfo();
    }

    public void setCommandContent(String value) {
        this.commandContent = value;
    }

    @Override
    protected void setInitialFocus() {
        this.setInitialFocus(this.commandEdit);
    }

    @Override
    protected @NotNull Component getUsageNarration() {
        return this.commandSuggestions.isVisible() ? this.commandSuggestions.getUsageNarration() : super.getUsageNarration();
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        String s = this.commandEdit.getValue();
        this.init(minecraft, width, height);
        this.commandEdit.setValue(s);
        this.commandSuggestions.updateCommandInfo();
    }

    protected void onDone() {
        PacketDistributor.sendToServer(new SetStaffCommandC2SPacket(this.commandEdit.getValue()));
        if (this.minecraft != null) {
            this.minecraft.setScreen(null);
        }
    }

    private void onEdited(String value) {
        this.commandSuggestions.updateCommandInfo();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.commandSuggestions.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode != 257 && keyCode != 335) {
            return false;
        } else {
            this.onDone();
            return true;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return this.commandSuggestions.mouseScrolled(scrollY) || super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.commandSuggestions.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, SET_COMMAND_LABEL, this.width / 2, 20, 16777215);
        guiGraphics.drawString(this.font, COMMAND_LABEL, this.width / 2 - 150 + 1, 40, 10526880);
        this.commandEdit.render(guiGraphics, mouseX, mouseY, partialTick);
        this.commandSuggestions.render(guiGraphics, mouseX, mouseY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderTransparentBackground(guiGraphics);
    }

}