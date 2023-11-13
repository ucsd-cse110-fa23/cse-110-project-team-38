package PantryPal.client;

public class MockRecorder implements IAudioRecorder {
    public String prompt;
    public String input;

    public MockRecorder(String input) {
        this.input = input;
    }
    
    public void startRecording() {
        prompt = input;
    }

    public void stopRecording() {
    }

    public String getRecording() {
        return prompt;
    }
}
