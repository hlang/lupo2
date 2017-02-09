import {Lupo2Page} from "./app.po";

describe('lupo2 App', function () {
    let page: Lupo2Page;

    beforeEach(() => {
        page = new Lupo2Page();
    });

    it('should display message saying app works', () => {
        page.navigateTo();
        expect(page.getParagraphText()).toEqual('app works!');
    });
});
