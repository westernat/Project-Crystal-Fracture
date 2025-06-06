package org.hismeo.nuquest.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.hismeo.crystallib.util.client.MinecraftUtil;
import org.hismeo.nuquest.api.dialog.ITextEffect;
import org.hismeo.nuquest.client.gui.component.ActionButton;
import org.hismeo.nuquest.core.dialog.ImageGroup;
import org.hismeo.nuquest.core.dialog.SoundGroup;
import org.hismeo.nuquest.core.dialog.context.DialogActionData;
import org.hismeo.nuquest.core.dialog.context.DialogDefinition;
import org.hismeo.nuquest.core.dialog.context.text.DialogText;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hismeo.crystallib.util.client.MinecraftUtil.getLevel;

public class DialogScreen extends Screen {
    private final Map<String, Number> varMap = new HashMap<>();
    protected Button flipButton;
    protected final List<ActionButton> actionButtons = new ArrayList<>();
    private final String dialogueId;
    private final DialogDefinition dialogDefinition;
    private final DialogActionData[] dialogActionDatas;
    private final DialogText[] dialogTexts;
    private String title;
    private ImageGroup imageGroup;
    private String originText;
    private String[] splitText;
    private SoundGroup soundGroup;
    private ITextEffect textEffect;
    private final int maxPage;
    private int page;
    private int initPage = -1;

    public DialogScreen(DialogDefinition dialogDefinition) {
        this(dialogDefinition, 0);
    }

    public DialogScreen(DialogDefinition dialogDefinition, int page) {
        super(CommonComponents.EMPTY);
        this.page = page;
        this.maxPage = dialogDefinition.dialogTexts().length;
        this.dialogDefinition = dialogDefinition;
        this.dialogActionDatas = dialogDefinition.dialogActionDatas();
        this.dialogueId = dialogDefinition.dialogueId();
        this.dialogTexts = dialogDefinition.dialogTexts();
    }

    @Override
    protected void init() {
        this.flipButton = this.addRenderableWidget(new ImageButton(this.width - 40,
                this.height - 40,
                20, 20,
                0, 0,
                20, Button.ACCESSIBILITY_TEXTURE,
                32, 64,
                this::tryFlip)
        );

        if (dialogActionDatas != null) {
            this.actionButtons.clear();
            for (int i = 0; i < this.dialogActionDatas.length; i++) {
                int finalI = i;
                ActionButton actionButton = new ActionButton(this.width - 100, this.height / 3 * 2 - (i + 1) * 30,
                        100, 20,
                        Component.literal(this.dialogActionDatas[i].message()),
                        () -> this.dialogActionDatas[finalI].action().action(this)
                );
                actionButton.hidden = true;
                this.addRenderableWidget(actionButton);
                this.actionButtons.add(actionButton);
            }
        }

        this.initPage();
    }

    @Override
    public void tick() {
        if (!canFlip()) {
            flipButton.active = false;
            actionButtons.forEach(button -> button.hidden = false);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int dialogueHeight = this.height / 3 * 2;
        guiGraphics.fillGradient(0, dialogueHeight, this.width, this.height, -1073741824, -1073741824);

        if (this.imageGroup != null && this.imageGroup.hasImage()) {
            this.imageGroup.blitImage(guiGraphics, varMap);
        }

        if (title != null) {
            MutableComponent translatable = Component.translatable(this.title);
            String string = translatable.getString().replace("playername", MinecraftUtil.getPlayer().getScoreboardName());
            guiGraphics.drawString(this.font, string, 10, dialogueHeight + 4, 0xFFFFFFFF);
            guiGraphics.fill(8, dialogueHeight + 4 + 10, 8 + 4 + this.font.width(string), dialogueHeight + 5 + 10, 0xFFFFFFFF);
        }
        for (int i = 0; i < this.splitText.length; i++) {
            guiGraphics.drawString(this.font, Component.translatable(this.splitText[i]), 20, dialogueHeight + 25 + (i * this.font.lineHeight + 4), 0xFFFFFFFF);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected void tryFlip(Button button) {
        if (canFlip()) {
            this.page++;
            this.initPage();
        }
    }

    protected boolean canFlip() {
        return this.page < this.maxPage - 1;
    }

    protected void initPage() {
        varMap.put("screenwidth", this.width);
        varMap.put("screenheight", this.height);
        if (initPage != page) {
            this.title = dialogTexts[page].title();
            this.imageGroup = dialogTexts[page].imageGroup();
            this.originText = dialogTexts[page].text();
            this.splitText = originText.split("\n");
            this.soundGroup = dialogTexts[page].soundGroup();
            this.textEffect = dialogTexts[page].textEffect();
            if (soundGroup != null) soundGroup.playSound(getLevel());
            initPage = page;
        }
    }
}

