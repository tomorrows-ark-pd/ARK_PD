package com.shatteredpixel.shatteredpixeldungeon.ui.changelist;

import com.shatteredpixel.shatteredpixeldungeon.messages.Messages;
import com.shatteredpixel.shatteredpixeldungeon.scenes.ChangesScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.ui.Icons;
import com.shatteredpixel.shatteredpixeldungeon.ui.Window;

import java.util.ArrayList;

public class v0_5_X_Changes {
    public static void addAllChanges(ArrayList<ChangeInfo> changeInfos) {
        add_v0_5_3_Changes(changeInfos);
        add_v0_5_2_Changes(changeInfos);
        add_v0_5_1_Changes(changeInfos);
        add_v0_5_0_Changes(changeInfos);
    }

    public static void add_v0_5_3_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.5.3", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "로도스 상점 추가",
                "로도스 오른쪽 아래에 클로저가 새로 상점을 차렸습니다. 정보증명서를 사용하여 아이템을 구매할 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.SCROLL), "퀘스트라인",
                "여러 목표를 연달아서 완료해야 하는 퀘스트를 추가하였습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP_WOND), "렌치",
                "특정 조합으로 얻을 수 있는 특별한 아이템입니다. 사용시 튼튼한 센트리를 소환하며, 3티어까지 강화 가능합니다. 센트리는 매 공격마다 체력을 소모하며, 렌치를 사용하서 체력을 회복 할 수 있습니다." + "\n\n" +
                        "1티어: 3턴마다 공격합니다." + "\n\n" +
                        "2티어: 1턴마다 공격합니다." + "\n\n" +
                        "3티어: 1턴마다 공격합니다. 4번째 공격마다 적들을 밀쳐내는 충격파를 발사합니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARTI), "종막 - 재난",
                "특수한 상점에서 볼 수 있는 특별한 유물입니다. 사용 시 특별한 소환수 3마리를 소환하며, 소환수들은 적을 찾아 빠른 속도로 움직이며, 처음 발견한 적에게 광역 피해를 입히고 사라집니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "도감 UI 개편",
                "도감 페이지가 완전히 새로 개편되었습니다. 게임 내 볼수 있는 아이템들과 적들은 도감에 등록 가능합니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "노트 UI 개편",
                "노트 페이지의 레이아웃이 개편되었습니다. 이전에 기록되던 항목들과 퀘스트들을 볼 수 있습니다." + "\n\n" +
                        "몇몇 퀘스트는 노트 UI를 통해 완료/포기가 가능합니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "로도스 확장 공사",
                "이제 상점 구역으로 갈 수 있습니다. 스킨 해금은 이제 골든글로우 대원을 통해 할 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그픽스",
                "풍차 수리검이 더이상 강화되지 않습니다." + "\n\n" +
                        "앙크 관련 버그들이 수정." + "\n\n" +
                        "여러 뱃지 관련 (표시, 해금) 버그 수정." + "\n\n" +
                        "이펙트 관련 크래시 버그 수정." + "\n\n" +
                        "박사를 들고 있을 경우 보너스 스테이지 적들의 어그로가 끌리는 버그 수정."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "상점 변경",
                "텍사스의 상점에 희귀한 물건들이 나오도록 변경했습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.BADGES), "뱃지 조정",
                "게임 플레이 관련 뱃지의 요구 게임플레이 횟수가 250/1000에서 100/250으로 조정되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "사격무기 변경",
                "사격 무기가 W의 함정을 발동 시키지 않도록 변경."));
        changeInfos.add(changes);
    }

    public static void add_v0_5_2_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.5.2", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changes.addButton(new ChangeButton(Icons.get(Icons.SCROLL), "의뢰",
                "로도스 2층에 있는 _도베르만_에게 말을 걸면 _의뢰_를 얻을 수 있습니다." + "\n\n" +
                        "의뢰 목표를 달성 후 _읽기_를 통해 보상을 받을 수 있습니다." + "\n\n" +
                        "향후 더 많은 의뢰가 추가될 예정입니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "로도스 이동",
                "_로도스_가 _27층_에서 _0층_으로 이사하였습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENTER), "게임 시작 위치",
                "게임 시작 위치가 _1층_에서 _로도스_로 옮겨졌습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARTI), "앙크 리워크",
                "_축복받지 않은 앙크_가 리워크 되었습니다." + "\n\n" +
                        "사망 시 _아이템 2개_를 선택하여 부활할 수 있으며, 죽은 위치에 있는 _가방_을 주울 경우 모든 아이템을 돌려받습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.RED), "레드 발리스틱 나이프",
                "기존 _자동 회수_ 기능이 삭제되었습니다." + "\n\n" +
                        "이제 _다른 층으로 이동_할 경우 자동으로 인벤토리에 나이프가 생성되며, 이동 후 전 층에 있는 나이프는 삭제됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.HERO_CHANGES), "기타 변경",
                "_훈련 인형_이 더 이상 정신지배의 영향을 받지 않습니다." + "\n\n" +
                        "_위매니의 의지_ 특수몹들이 오퍼레이터의 공격을 받지 않는 이상 활성화되지 않습니다." + "\n\n" +
                        "_위매니의 의지_ 돌진 능력이 리워크 되었습니다. 이제 돌진을 여러 번 연속으로 발동하며, 적중 시 _분노 스택_이 증가합니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정",
                "_빛의 축복 +1_이 저주를 약화만 시키던 버그를 수정했습니다." + "\n\n" +
                        "여러 _텍스트 누락_을 수정했습니다." + "\n\n" +
                        "_특공대원_의 공격이 주술과 약화를 걸지 않던 버그를 수정했습니다." + "\n\n" +
                        "_일개미_가 저주받은 무기를 떨어뜨릴 수 있던 버그를 수정했습니다." + "\n\n" +
                        "_살라스의 칼날_ 데미지 증가가 100% 아래에선 적용되지 않던 버그를 수정했습니다." + "\n\n" +
                        "_갑옷 강화 키트_가 아츠를 제대로 옮기지 않던 버그를 수정했습니다." + "\n\n" +
                        "여러 식사 특성과 _굼 인형_이 정상적으로 작동하지 않던 버그를 수정했습니다." + "\n\n" +
                        "공격속도가 느린 행동으로 _폭탄새_ 공격 시 바로 터지던 버그를 수정했습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.RING), "장비 상향",
                "_햇빛의 반지_가 스킬 사용에 필요한 _SP_를 감소시킵니다." + "\n\n" +
                        "_이세계 전술 장비 - 폭발물_이 이제 투척한 칸에서 튕겨나가지 않습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENEMY_BUFFS), "제국 포화 선조자 상향",
                "_제국 포화 선조자_가 이제 모든 아군을 타겟으로 지정할 수 있습니다." + "\n\n" +
                        "가장 가까운 아군 유닛을 우선적으로 공격하며, _시야 밖_으로 나갈 경우 타겟이 되지 않습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.NEARL), "니어 상향",
                "_기사_ 직군이 _신속_ 보유 시 공격속도가 _20%_ 증가합니다." + "\n\n" +
                        "_재림_ 특성이 이제 축복된 앙크의 무적시간을 1/2턴 증가시킵니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "샷건 상향",
                "_샷건_이 적중한 대상 방어도의 영향을 덜 받도록 수정되었습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.NEARL), "니어 하향",
                "_빛의 기사_의 데미지 증가율이 너프되었습니다. (기본 +30% -> +25%, 구제자 +45% -> +40%, 섬광 +20% -> +15%)" + "\n\n" +
                        "_빛의 기사_의 받는 피해 감소량이 너프되었습니다. (기본 2 ~ 피해량의 20% -> 1 ~ 피해량의 20%, 구제자 2 ~ 피해량의 40% -> 3 ~ 피해량의 30%)" + "\n\n" +
                        "_섬광_의 빛의 인장 광역딜 데미지가 1(+1) ~ 6(+2)에서 1(+1) ~ 4(+2)로 너프되었습니다." + "\n\n" +
                        "_구제자_의 방패 밀기 스택 데미지 배율이 x0.25 ~ x2(+0.1)에서 x0.5 ~ x1.5(+0.05)로 너프되었습니다." + "\n\n" +
                        "_구제자_의 방패 밀기의 밀치기와 마비가 피해 축적량 최대치의 절반 이상일 경우에만 발동됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENEMY_NERFS), "적 하향",
                "_폭탄새_의 폭발 데미지가 21~29에서 _18~25_로 너프되었습니다." + "\n\n" +
                        "_위매니의 의지_의 돌진 능력 쿨다운이 증가되었습니다."));
        changeInfos.add(changes);
    }

    public static void add_v0_5_1_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.5.1", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "산탄총",
                "새로운 사격무기 종류인 _산탄총_이 추가되었습니다. 탄약수는 적지만 광역딜과 강력한 단일 딜링 능력을 가진 무기입니다." + "\n\n" +
                        "_캐시디_ 2티어 산탄총입니다. 2발만 장전 가능하지만 대가로 매우 강한 공격력을 가진 샷건입니다." + "\n\n" +
                        "_SG CQB_ 4티어 산탄총입니다. 무난한 샷건입니다." + "\n\n" +
                        "_M870_ 5티어 산탄총입니다. 더욱 많은 펠릿을 발사하지만 집탄률이 낮습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "부착물 변경",
                "_부착물_들이 산탄총에 대한 전용 효과를 가지게 되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ALERT), "폭발새 변경",
                "_폭발새_이 1턴 딜레이 후 폭발하도록 변경되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "무기 스킬 변경",
                "공격형 _무기 스킬_ 사용 시 투명 해제 및 시간정지가 중단됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "머드락 스킨",
                "_머드락_ 스킨 해금 조건이 변경되었습니다: 협약 3개 이상으로 클리어"));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정",
                "_위매니의 의지_가 더 이상 침묵에 면역이 아닙니다." + "\n\n" +
                        "아이템이 _퀵슬롯_에서 정상적으로 유지되도록 수정했습니다." + "\n\n" +
                        "_신경손상_이 음수로 내려가지 않도록 수정했습니다." + "\n\n" +
                        "_빛의 인장_이 로도스에서 충전되지 않도록 수정했습니다." + "\n\n" +
                        "다양한 크래시 및 프리즈를 수정했습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "사격무기 상향",
                "재장전 시간이 2턴으로 변경되었습니다. (OTs-03, DP27, 6P41은 3턴 유지)" + "\n\n" +
                        "더 많은 탄약을 가진 채로 드롭됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP_WOND), "스태프 상향",
                "_스태프 오브 리프_ 데미지가 1~5(+5)에서 2~6(+5)로 증가했습니다." + "\n\n" +
                        "_스태프 오브 그레이_ 데미지가 3(+1)~8(+4)에서 6(+1)~9(+4)로 증가했습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENEMY_BUFFS), "폭발새 상향",
                "폭발 데미지가 17~25에서 21~29로 증가했습니다." + "\n\n" +
                        "더이상 폭발 데미지가 방어도의 두배 효과를 받지 않습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP_WOND), "스태프 오브 비그나 하향",
                "치명타 데미지가 +50%에서 +35%로 감소했습니다." + "\n\n" +
                        "치명타 확률이 감소했습니다. +12에서 확정 치명타가 발생합니다." + "\n\n" +
                        "데미지가 4(+1)~8(+6)에서 4(+1)~8(+5)로 감소했습니다."));
        changeInfos.add(changes);
    }

    public static void add_v0_5_0_Changes(ArrayList<ChangeInfo> changeInfos) {
        ChangeInfo changes = new ChangeInfo("v0.5.0", true, "");
        changes.hardlight(Window.TITLE_COLOR);
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "new"), false, null);
        changes.hardlight(Window.TITLE_COLOR);
        changes.addButton(new ChangeButton(Icons.get(Icons.DEPTH), "이베리아",
                "새로운 보스 3종이 추가되었습니다." + "\n\n" +
                        "새로운 기믹인 _신경손상_과 _명흔_이 추가되었습니다." + "\n\n" +
                        "새로운 퀘스트가 추가되었습니다. 33층 랜덤한 방에서 찾을 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "사격무기",
                "새로운 무기 종류인 _사격 무기_가 추가되었습니다. 투척 무기로 재장전 하여, 일정 사거리 이내 적을 안전하게 처리할 수 있는 원거리 위주 무기군입니다. 추가로 다양한 부착물을 이용해 스텟을 변경 할 수 있습니다." + "\n\n" +
                        "_USG57_ 2티어 무기입니다. 기본적인 사격무기입니다. 사거리가 짧지만, 근접 페널피를 받지 않습니다." + "\n\n" +
                        "_화이트팽 465_ 3티어 무기로, 무난한 돌격소총입니다." + "\n\n" +
                        "_스털링 기관단총_ 3티어 무기로, 확률적으로 적을 한기상태로 만듭니다." + "\n\n" +
                        "_DP 27_ 4티어 무기로, 많은 양의 탄약을 장전할 수 있지만 정확도가 떨어집니다." + "\n\n" +
                        "_OTs-03_ 4티어 무기로, 강력한 데미지를 가졌지만 사격속도가 매우 느립니다." + "\n\n" +
                        "_6P41_ 5티어 무기로, 더욱 많은 양의 탄약을 장전할 수 있지만 정확도가 떨어집니다." + "\n\n" +
                        "_R4-C_ 5티어 무기로, 강력한 돌격소총입니다." + "\n\n"));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "클로저 무한변환상자",
                "로도스 상점에 _무한변환상자_가 추가되었습니다." + "\n\n" +
                        "반복 사용이 가능한 변환 아이템으로, 사용할 때마다 가격이 증가합니다." + "\n\n" +
                        "낮은 확률로 특별한 아이템으로 변환될 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.NAMSEK), "가비알 비주얼 리워크",
                "가비알 스테이지 _36-40층_의 비주얼이 새로 추가되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.SEED_POUCH), "고정 시드",
                "고정된 시드로 게임을 시작 할 수 있는 기능이 추가되었습니다." + "\n\n" +
                        "캐릭터 선택 화면에서 설정을 통해 시드 고정이 가능하며, 시드 고정시 뱃지 및 스킨 해금이 불가능합니다. 또한, 랭킹 페이지에서 고정된 시드 플레이는 최하단에 표시됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.DANWOO), "기타 편의성 업데이트",
                "박사 구출 후 켈시에게 말을 걸어도 게임이 끝나지 않습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "changes"), false, null);
        changes.hardlight(CharSprite.WARNING);
        changeInfos.add(changes);
        changes.addButton(new ChangeButton(Icons.get(Icons.NEARL), "니어 리워크",
                "_빛의 인장_이 최대 10레벨까지 성장할 수 있게 되었습니다." + "\n\n" +
                        "_섬광_ 직군은 빛의 인장 발동시 시야 내 모든 적에게 데미지를 줍니다." + "\n\n" +
                        "_구제자_ 직군은 새로운 액티브 스킬 _방패 밀기_가 추가되었습니다. 피해를 받을수록 충전되며, 데미지와 마비, 넉백을 줍니다." + "\n\n" +
                        "_기사_ 직군은 빛의 인장 발동시 더 이상 턴을 소모하지 않습니다." + "\n\n" +
                        "여러 특성이 변경되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.AMIYA), "아미야 변경",
                "_스태프 개조 키트_로 _흑요석 반지_의 무기 티어를 영구적으로 2단계 올릴 수 있습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP_WOND), "스태프 리워크",
                "_스태프 오브 브리즈_ 힐링/부식 모드를 전환할 수 있게 되었습니다." + "\n\n" +
                        "_스태프 오브 퍼거토리_ 위치 변환 후 양쪽 위치에 범위 폭발 데미지를 줍니다." + "\n\n" +
                        "_스태프 오브 샤이닝_ 적중시 보호막을 얻습니다. 아군에게 보호막을 부여합니다." + "\n\n" +
                        "_스태프 오브 스노우상트_ 감염 속성 적에게 추가 데미지, 침묵 후 확률적으로 감속을 부여합니다." + "\n\n" +
                        "_스태프 오브 비그나_ 확률적으로 1.5배 치명타가 발생합니다." + "\n\n" +
                        "_스태프 오브 위디_ 적이 낭떠러지로 떨어질 경우 충전을 일부 회수합니다." + "\n\n" +
                        "_스태프 오브 커럽팅_ 소환수에게 지속 회복 효과가 추가되었습니다." + "\n\n" +
                        "_스태프 오브 그레이_ 50% 확률로 감속을 부여합니다." + "\n\n" +
                        "_스태프 오브 포덴코_ 대상에게 독을 추가 부여합니다." + "\n\n" +
                        "_스태프 오브 스즈란_ 피격 대상에게 취약을 부여합니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.RING), "반지 변경",
                "_명중의 반지_ 사격무기에 명중 보너스가 적용됩니다." + "\n\n" +
                        "_분노의 반지_ 사격무기에 공격속도 보너스가 절반 적용됩니다." + "\n\n" +
                        "_저격의 반지_ 사격무기는 탄환 소모 방지와 사거리 보너스가 적용됩니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정 1",
                "_절지생화_의 분신 소환시 간헐적으로 발생하던 크래시를 수정했습니다." + "\n\n" +
                        "_불사의 검은 뱀_의 페이즈가 진행되지 않던 버그를 수정했습니다." + "\n\n" +
                        "_정보증명서_가 제대로 저장 및 로드되지 않든 버그를 수정했습니다." + "\n\n" +
                        "_자연의 은총_및 잔디 생성이 맵 가장자리에서 발동 할 경우 발생하던 크래시를 수정했습니다" + "\n\n" +
                        "_베오울프_의 사거리가 4 이상일때 데미지 감소 페널티가 적용되던 버그를 수정했습니다" + "\n\n" +
                        "_EX-42_에 _도달의 아츠_가 있을 경우 사거리가 1로 감소하던 버그를 수정했습니다" + "\n\n" +
                        "_검술 아미야_의 _푸른 분노_특성이 표기대로 적용되지 않던 버그를 수정했습니다" + "\n\n" +
                        "_홀로 가는 먼 길_의 특수기술이 충전되지 않은 상태로도 사용 가능하던 버그를 수정했습니다"));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정 2",
                "_탈룰라_가 정보증명서를 드롭하지 않던 버그를 수정했습니다." + "\n\n" +
                        "_지배의 반지_를 착용했을 때 발생하던 크래시를 수정했습니다." + "\n\n" +
                        "_나기나타_의 처형 발동 시 데미지 숫자가 출력되지 않도록 수정했습니다." + "\n\n" +
                        "_레드_의 _뼈찌르기_의 대상을 본인으로 지정하지 못하도록 수정했습니다." + "\n\n" +
                        "앱을 백그라운드로 전환 시 저장이 제대로 이루어지지 않아, 복귀 시 앱이 강제 종료되던 현상을 수정했습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.BUG_KILL), "버그 수정 3",
                "로도스가 매번 재생성되던 버그를 수정했습니다." + "\n\n" +
                        "_이리떼_ 스킬이 정확하지 않은 칸에 마비를 부여하던 버그를 수정했습니다." + "\n\n" +
                        "_혼돈의 정수_가 아츠 지팡이류 아이템을 정상적으로 강화하지 않던 버그를 수정했습니다." + "\n\n" +
                        "_운명_ 스킬 사용 시 앱이 강제 종료되던 버그를 수정했습니다." + "\n\n" +
                        "레드의 _발리스틱 나이프_가 가끔 사라지던 버그를 수정했습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.ARKPD), "UI 업데이트",
                "여러 UI요소들이 업데이트 되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.DANWOO), "안드로이드 5.0 미만 지원중단",
                "자세한 내용은 0.5.0-B4 변경사항 코멘트를 참고 해 주세요."));

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "buffs"), false, null);
        changes.hardlight(CharSprite.POSITIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.NEARL), "니어 상향",
                "_빛의 인장_의 충전 속도가 증가되었습니다. (기본 턴마다 0.34 -> 0.50)"));
        changes.addButton(new ChangeButton(Icons.get(Icons.WEP), "무기 상향",
                "_LL-200_ 필중 효과가 추가되었습니다." + "\n\n" +
                        "_베오울프_ 장착시 방어력을 증가시킵니다. 거리 조건이 4칸에서 3칸으로 완화되었습니다." + "\n\n" +
                        "_불꽃의 카타나_ 해방 조건이 125킬에서 85킬로 완화되었습니다."));
        changes.addButton(new ChangeButton(Icons.get(Icons.NAMSEK), "정보증명서",
                "보스들이 드롭하는 정보증명서의 양이 증가되었습니다."));
        changeInfos.add(changes);

        changes = new ChangeInfo(Messages.get(ChangesScene.class, "nerfs"), false, null);
        changes.hardlight(CharSprite.NEGATIVE);
        changes.addButton(new ChangeButton(Icons.get(Icons.NEARL), "니어 하향",
                "_빛의 기사_ 공격 배율이 감소되었습니다. (1.4x -> 1.3x, 구제자 1.55x -> 1.45x, 섬광 1.25x -> 1.2x)"));
        changes.addButton(new ChangeButton(Icons.get(Icons.ENEMY_NERFS), "적 하향",
                "_가비알_ 몹들의 체력과 회피도가 하향되었습니다."));
        changeInfos.add(changes);
    }
}
